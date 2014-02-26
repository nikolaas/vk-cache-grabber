package org.ns.vkcachegrabber.vk.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.ns.func.Function;
import org.ns.ioc.IoC;
import org.ns.util.Assert;
import org.ns.util.Strings;
import org.ns.util.Utils;
import org.ns.vkcachegrabber.Openables;
import org.ns.vkcachegrabber.api.Account;
import org.ns.vkcachegrabber.vk.AuthService;
import org.ns.vkcachegrabber.api.Application;
import org.ns.vkcachegrabber.vk.Credential;
import org.ns.vkcachegrabber.vk.CredentialProvider;
import org.ns.vkcachegrabber.api.DocumentManager;
import org.ns.vkcachegrabber.api.OpenContext;
import org.ns.vkcachegrabber.vk.AccessToken;
import org.ns.vkcachegrabber.vk.VKMethod;
import org.ns.vkcachegrabber.vk.VkAuthException;
import org.ns.vkcachegrabber.vk.VkException;
import org.ns.store.DataStore;
import org.ns.store.DataStoreFactory;

/**
 *
 * @author stupak
 */
public class Autorizator implements AuthService {

    private static final Logger logger = Logger.getLogger(Autorizator.class.getName());
    
    public static final String CURRENT_USER_ID = "currentUserId";
    public static final String ACCOUNT_STORAGE = "accountStorage";
    
    private final List<AccountImpl> accounts = new ArrayList<>();;
    private final Application application;
    private final DataStoreFactory accountStorageFactory;
    private final DataStoreFactory accessTokenStorageFactory;
    private final CredentialProvider credentialProvider;
    private final HttpClient httpClient;
    private final AuthFunction authFunction = new AuthFunction();
    
    private String currentUserId;
    private DataStore<AccountImpl> accountStorage;
    private DataStore<AccessToken> accessTokenStorage;
    
    private final VKMethod authorizeMethod;
    private final VKMethod loginMethod;
    
    private Thread parked;
    private final Lock lock = new ReentrantLock();
    
    public static class Builder {
        private Application application;
        private DataStoreFactory accountStorageFactory;
        private DataStoreFactory accessTokenStorageFactory;
        private CredentialProvider credentialProvider;

        public Builder application(Application application) {
            Assert.isNotNull(application, "application is null");
            this.application = application;
            return this;
        }

        public Builder setAccountStorageFactory(DataStoreFactory accountStorageFactory) {
            this.accountStorageFactory = accountStorageFactory;
            return this;
        }

        public Builder setAccessTokenStorageFactory(DataStoreFactory accessTokenStorageFactory) {
            this.accessTokenStorageFactory = accessTokenStorageFactory;
            return this;
        }

        public Builder setCredentialProvider(CredentialProvider credentialProvider) {
            Assert.isNotNull(application, "credentialProvider is null");
            this.credentialProvider = credentialProvider;
            return this;
        }
        
        public Autorizator build() {
            return new Autorizator(this);
        }
    }
    
    private Autorizator(Builder builder) {
        this.application = builder.application;
        this.accountStorageFactory = builder.accountStorageFactory;
        this.accessTokenStorageFactory = builder.accessTokenStorageFactory;
        this.credentialProvider = builder.credentialProvider;
        this.currentUserId = this.application.getConfig().get(CURRENT_USER_ID);
        this.httpClient = IoC.get(HttpClient.class);
        this.authorizeMethod = VKMethod.authorizeMethodTemplate()
                .param(VKMethod.VK_CLIENT_ID, IoC.get(Application.class).getVkClientId())
                .param(VKMethod.VK_REDIRECT_URL, VKMethod.DEFAULT_REDIRECT_URL)
                .param(VKMethod.VK_DISPLAY, VKMethod.VK_DISPLAY_POPUP)
                .param(VKMethod.VK_SCOPE, VKMethod.VK_SCOPE_AUDIO)
                .param(VKMethod.VK_RESPONSE_TYPE, VKMethod.VK_RESPONSE_TYPE_TOKEN)
                .param(VKMethod.VK_API_VERSION, VKMethod.VK_API_VERSION_LAST)
                .build();
        this.loginMethod = VKMethod.loginMethodTemplate()
                .param("act", "login")
                .param("soft", "1")
                .param("q", "1")
                .param("from_host", "oauth.vk.com")
                .param("expire", "0")
                .build();
    }
    
    @Override
    public Account getCurrentAccount() {
        return getAccount(currentUserId);
    }
    
    private void saveAccountToStorage(AccountImpl account) {
        DataStore<AccountImpl> storage = getAccountStorage();
        if ( storage != null ) {
            try {
                storage.set(account.getUserId(), account);
            } catch (IOException ex) {
                Logger.getLogger(Autorizator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private DataStore<AccountImpl> getAccountStorage() {
        if ( accountStorage == null ) {
            if ( accountStorageFactory != null ) {
                try {
                    accountStorage = accountStorageFactory.getDataStore(ACCOUNT_STORAGE);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "accountSotage init error", ex);
                }
            }
        }
        return accountStorage;
    }

    private DataStore<AccessToken> getAccessTokenStorage() {
        if ( accessTokenStorage == null ) {
            if ( accessTokenStorageFactory != null ) {
                try {
                    accessTokenStorage = accessTokenStorageFactory.getDataStore(Application.ACCESS_TOKEN_STORAGE);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "accessTokenStorage init error", ex);
                }
            }
        }
        return accessTokenStorage;
    }
    
    public AccessToken loadAccessTokenFromStorage(String userId) {
        DataStore<AccessToken> storage = getAccessTokenStorage();
        if ( storage == null ) {
            return null;
        }
        AccessToken stored = null;
        try {
            stored = storage.get(userId);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "load access token error", ex);
        }
        return stored;
    }
    
    public void saveAccessTokenToStorage(AccessToken token) {
        DataStore<AccessToken> storage = getAccessTokenStorage();
        if ( storage == null ) {
            return;
        }
        try {
            storage.set(token.getUserId(), token);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "save access token error", ex);
        }
    }
    
    private class AuthFunction implements Function<AuthContext, AccountImpl> {

        @Override
        public AccountImpl apply(AuthContext context) throws Exception {
            AccountImpl account = null;
            if ( !Strings.empty(currentUserId) ) {
                account = getAccount(currentUserId);
            }
            if ( account == null ) {//текущего акаунта нет
                //тут мы авторизуемся и получаем новый токен
                account = doAuthorize();
                if ( account != null ) {
                    accounts.add(account);
                    saveAccountToStorage(account);
                }
            } else {//тут у нас уже есть акаунт
                //нужно проверить токен для него, и если его нет - получить
                AccessToken accessToken = account.getAccessToken();
                if ( accessToken == null ) {
                    accessToken = loadAccessTokenFromStorage(account.getUserId());
                    if ( accessToken != null ) {
                        account.setAccessToken(accessToken);
                    }
                }
                if ( accessToken == null ) {
                    updateAccessToken(account);
                    saveAccessTokenToStorage(account.getAccessToken());
                }
            }
            currentUserId = account == null ? null : account.getUserId();
            return account;
        }
        
    }
    
    @Override
    public void authorize() throws VkAuthException {
        auth(new AuthContext().requestNew(false));
        logger.log(Level.INFO, "authorize success. currentUserId={0}", currentUserId);
    }

    @Override
    public void reauthorize() throws VkAuthException {
        auth(new AuthContext().requestNew(true));
        logger.log(Level.INFO, "reauthorize success. currentUserId={0}", currentUserId);
    }
    
    @Override
    public AccessToken getAccessToken() throws VkAuthException {
        AccountImpl account = auth(new AuthContext().requestNew(false));
        AccessToken accessToken = account.getAccessToken();
        logger.log(Level.INFO, "getAccessToken success. currentUserId={0}", currentUserId);
        return accessToken;
    }
    
    private AccountImpl auth(AuthContext context) throws VkAuthException {
        AccountImpl account = null;
        try {
            account = authFunction.apply(context);
        } catch (Exception ex) {
            String mes = "Ошибка авторизации: " + ex.getMessage();
            throw new VkAuthException(mes, ex);
        }
        return account;
    }
    
    private AccountImpl getAccount(String userId) {
        AccountImpl account = findLoaded(userId);
        if ( account == null ) {
            try {
                DataStore<AccountImpl> storage = getAccountStorage();
                if ( storage != null ) {
                    account = storage.get(userId);
                }
            } catch (IOException ex) {
                Logger.getLogger(Autorizator.class.getName()).log(Level.SEVERE, null, ex);
            }
            if ( account != null ) {
                accounts.add(account);
            }
        }
        return account;
    }
    
    private AccountImpl findLoaded(String userId) {
        for ( AccountImpl account : accounts ) {
            if ( Objects.equals(account.getUserId(), userId) ) {
                return account;
            }
        }
        return null;
    }
    
    private AccountImpl doAuthorize() throws VkException {
        return authorizeImpl(null);
    }
    
    private AccountImpl updateAccessToken(AccountImpl account) throws VkException {
        return authorizeImpl(account);
    }
    
    private AccountImpl authorizeImpl(AccountImpl account) throws VkException {
        try {
            HttpUriRequest request = RpcUtils.toHttpRequest(authorizeMethod);
            HttpResponse response = httpClient.execute(request);
            request.abort();
            String grantAccessRedirect = findHeaderValue(response, "location");
            if ( grantAccessRedirect == null ) {
                String userId = account == null ? null : account.getUserId();
                Credential credential = credentialUserRequest(userId);
                //еще не авторизованы, авторизуемся
                if ( credential == null ) {
                    throw new VkException("Неверный логин или пароль");
                }
                String content = RpcUtils.read(response.getEntity().getContent());
                String ip_h = RpcUtils.findParamValue(content, "ip_h");
                String to = RpcUtils.findParamValue(content, "to");

                loginMethod.setParam("ip_h", ip_h);
                loginMethod.setParam("to", to);
                loginMethod.setParam("email", credential.getEmail());
                loginMethod.setParam("pass", Credential.toString(credential.getPassword()));
                request = RpcUtils.toHttpRequest(loginMethod);
                response = httpClient.execute(request);
                request.abort();
                // Получили редирект на подтверждение требований приложения
                grantAccessRedirect = findHeaderValue(response, "location");
            
            }
            request = new HttpPost(grantAccessRedirect);
            // Проходим по нему
            response = httpClient.execute(request);
            request.abort();
            
            // Теперь последний редирект на получение токена
            String accessTokenRedirect = findHeaderValue(response, "location");
            if ( accessTokenRedirect == null ) {
                //если уже права еще неполучены, то accessTokenRedirect будет в форме, которую нам отправили
                String content = RpcUtils.read(response.getEntity().getContent());
                accessTokenRedirect = RpcUtils.findValue(content, "action");
            }
            // Проходим по нему
            request = new HttpPost(accessTokenRedirect);
            response = httpClient.execute(request);
            request.abort();
            String accessTokenResponceUri = findHeaderValue(response, "location");
            Map<String, String> accessTokenMap = Openables.getParams(accessTokenResponceUri);
            AccessToken newToken = AccessToken.from(accessTokenMap);
            if ( account == null ) {
                account = new AccountImpl();
                account.setUserId(newToken.getUserId());
            }
            account.setAccessToken(newToken);
        } catch (IOException ex) {
            throw new VkException(ex);
        }
        return account;
    }
    
    private String findHeaderValue(HttpResponse response, String headerName) {
        Header header = response.getFirstHeader(headerName);
        if ( header != null ) {
            return header.getValue();
        } else {
            return null;
        }
    }
    
    private Credential credentialUserRequest(String userId) {
        if ( !Strings.empty(userId) ) {
            Credential credential = credentialProvider.getCredential(userId);
            if ( credential != null ) {
                return credential;
            }
        }
        final CredentialHolder credentialHolder = new CredentialHolder();
        final DocumentManager.ResultReceiver receiver = new DocumentManager.ResultReceiver() {

            @Override
            public void onResult(int statusCode, Object result) {
                Credential credential = (Credential) result;
                credentialHolder.credential = credential;
                LockSupport.unpark(parked);
                parked = null;
            }
        };
        Utils.invokeWhenUIReady(new Runnable() {

            @Override
            public void run() {
                OpenContext context = new OpenContext();
                application.getDocumentManager().openForResult(Openables.buildAuthOpenable(), context, receiver);
            }
        });
        waitForGUI();
        return credentialHolder.credential;
    }

    private static final class CredentialHolder {
        private Credential credential;
    }
    
    private void waitForGUI() {
        lock.lock();
        try {
            parked = Thread.currentThread();
            LockSupport.park(lock);
        } finally {
            lock.unlock();
        }
    }
    
    private void showError(final DocumentManager documentManager, String message, Throwable th) {
        Utils.invokeWhenUIReady(new Runnable() {

            @Override
            public void run() {
                documentManager.open(Openables.buildErrorOpenable(), new OpenContext());
            }
        });
    }
    
    public static interface WakeUpHandler {
        void wakeUp();
    }
    
    public class AccountImpl implements Account, Serializable {

        private String userId;
        private String userName;
        private AccessToken accessToken;

        @Override
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public AccessToken getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(AccessToken accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + Objects.hashCode(this.userId);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Account other = (Account) obj;
            if ( !Objects.equals(this.userId, other.getUserId())) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Account{" + "userId=" + userId + ", userName=" + userName + '}';
        }

    }
    
    private static class AuthContext {
        private boolean requestNew;

        public boolean isRequestNew() {
            return requestNew;
        }

        public void setRequestNew(boolean requestNew) {
            this.requestNew = requestNew;
        }
        
        public AuthContext requestNew(boolean requestNew) {
            this.requestNew = requestNew;
            return this;
        }
    }
}
