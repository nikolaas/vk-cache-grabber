package org.ns.vk.cachegrabber.api.vk.impl;

import org.ns.vk.cachegrabber.api.vk.AccessToken;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.ns.ioc.IoC;
import org.ns.vk.cachegrabber.api.Account;
import org.ns.vk.cachegrabber.api.AccountManager;
import org.ns.vk.cachegrabber.api.Application;
import org.ns.vk.cachegrabber.api.vk.AccessTokenProvider;
import org.ns.vk.cachegrabber.api.Credential;
import org.ns.vk.cachegrabber.api.CredentialProvider;
import org.ns.vk.cachegrabber.api.vk.VKMethod;
import org.ns.vk.cachegrabber.store.DataStore;
import org.ns.vk.cachegrabber.store.DataStoreFactory;

/**
 *
 * @author stupak
 */
public class AccessTokenProviderImpl implements AccessTokenProvider {

    private final HttpClient httpClient;
    private final VKMethod authorizeMethod;
    private final VKMethod loginMethod;
    private AccessToken accessToken;
    private final DataStoreFactory accessTokenDataStoreFactory;
    private DataStore<AccessToken> accessTokenStorage;
    
    public AccessTokenProviderImpl(DataStoreFactory accessTokenDataStoreFactory) {
        this.accessTokenDataStoreFactory = accessTokenDataStoreFactory;
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
    public AccessToken getAccessToken() {
        Account account = IoC.get(Application.class).getService(AccountManager.class).getCurrentAccount();
        if ( accessToken != null ) {
            if ( !checkForLiveToken(accessToken) ) {
                accessToken = requestNewAccessToken(account);
            }
        } else {
            try {
                accessToken = loadAccessTokenFromStorage(account.getUserId());
            } catch (IOException ex) {
                Logger.getLogger(AccessTokenProviderImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            if ( accessToken == null  || !checkForLiveToken(accessToken) ) {
                accessToken = requestNewAccessToken(account);
            }
        }
        return accessToken;
    }
    
    public AccessToken loadAccessTokenFromStorage(String userId) throws IOException {
        DataStore<AccessToken> storage = getAccessTokenStorage();
        if ( storage == null ) {
            return null;
        }
        AccessToken stored = storage.get(userId);
        if (stored == null) {
            return null;
        }
        return stored;
    }
    
    private DataStore<AccessToken> getAccessTokenStorage() {
        if ( accessTokenStorage == null ) {
            if ( accessTokenDataStoreFactory != null ) {
                try {
                    accessTokenStorage = accessTokenDataStoreFactory.getDataStore(Application.ACCESS_TOKEN_STORAGE);
                } catch (IOException ex) {
                    Logger.getLogger(AccessTokenProviderImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return accessTokenStorage;
    }
    
    private boolean checkForLiveToken(AccessToken token) {
        long current = System.currentTimeMillis() / 1000;
//            if ( current - accessToken.getCreated() < accessToken.getExpiresIn() ) {
        //return token != null && (token.getRefreshToken() != null || token.getExpiresInSeconds() > 60);
        return false;
    }
    
    private AccessToken requestNewAccessToken(Account account) {
        Credential credential = IoC.get(Application.class).getService(CredentialProvider.class).getCredential(account.getUserId());
        if ( credential == null ) {
            return null;
        }
        AccessToken newToken = null;
        try {
            HttpUriRequest request = RpcUtils.toHttpRequest(authorizeMethod);
            HttpResponse response = httpClient.execute(request);
            request.abort();
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
            String headerLocation = response.getFirstHeader("location").getValue();
            request = new HttpPost(headerLocation);
            // Проходим по нему
            response = httpClient.execute(request);
            request.abort();
            // Теперь последний редирект на получение токена
            headerLocation = response.getFirstHeader("location").getValue();
            // Проходим по нему
            request = new HttpPost(headerLocation);
            response = httpClient.execute(request);
            request.abort();
            Map<String, String> accessTokenMap = new HashMap<>();
            headerLocation = response.getFirstHeader("location").getValue();
            int paramStringStart = headerLocation.indexOf("#") + 1;
            String paramString = headerLocation.substring(paramStringStart);
            String[] params = paramString.split("&");
            for ( String param : params ) {
                String[] parsedParam = param.split("=");
                String paramName = parsedParam[0];
                String paramValue = parsedParam[1];
                accessTokenMap.put(paramName, paramValue);
            }
            newToken = AccessToken.from(accessTokenMap);
        } catch (IOException ex) {
            Logger.getLogger(AccessTokenProviderImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newToken;
    }
       
}
