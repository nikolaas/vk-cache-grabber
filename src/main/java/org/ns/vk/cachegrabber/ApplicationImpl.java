package org.ns.vk.cachegrabber;

import java.awt.Container;
import org.ns.vk.cachegrabber.api.vk.impl.AccessTokenProviderImpl;
import org.ns.vk.cachegrabber.api.vk.impl.VKApiImpl;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.http.client.HttpClient;
import org.ns.ioc.IoC;
import org.ns.vk.cachegrabber.api.AccountManager;
import org.ns.vk.cachegrabber.api.Application;
import org.ns.vk.cachegrabber.api.CredentialProvider;
import org.ns.vk.cachegrabber.api.vk.AccessTokenProvider;
import org.ns.vk.cachegrabber.api.vk.Audio;
import org.ns.vk.cachegrabber.api.vk.VKApi;
import org.ns.vk.cachegrabber.api.vk.VKObjectFactory;
import org.ns.vk.cachegrabber.api.vk.impl.RPC;
import org.ns.vk.cachegrabber.api.UriHandlerRegistry;
import org.ns.vk.cachegrabber.json.AudioJSONConverter;
import org.ns.vk.cachegrabber.json.JSONConverterRegistry;
import org.ns.vk.cachegrabber.store.DataStoreFactory;
import org.ns.vk.cachegrabber.store.FileDataStoreFactory;

/**
 *
 * @author stupak
 */
class ApplicationImpl implements Application {

    private static final String VK_CLIENT_ID = "4199664";
    
    private final Window mainWindow;
    private final Container contentPane;
    private final VKApi vkApi;
    private final Map<Class<?>, Object> services;
    private Config config;

    public ApplicationImpl(JFrame mainWindow) {
        this.mainWindow = mainWindow;
        this.contentPane = mainWindow.getContentPane();
        this.vkApi = new VKApiImpl();
        this.services = new HashMap<>();
    }
            
    @Override
    public Window getMainWindow() {
        return mainWindow;
    }

    @Override
    public Container getContentPane() {
        return contentPane;
    }
    
    @Override
    public VKApi getVKApi() {
        return vkApi;
    }

    @Override
    public String getVkClientId() {
        return VK_CLIENT_ID;
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        Object service = services.get(serviceClass);
        if ( service != null ) {
            return serviceClass.cast(service);
        } else {
            return null;
        }
    }
    
    void start() {
        config = new Config();
        initInnerServices();
        initServices();
        SwingUtilities.invokeLater(new GuiInitializer(this));
    }
    
    private void initInnerServices() {
        register(UriHandlerRegistry.class, new UriHandlerRegistryImpl());
        String accountsStorage = (String) config.get(ACCOUNT_STORAGE);
        DataStoreFactory accountStorageFactory = null;
        try {
            accountStorageFactory = new FileDataStoreFactory(new File(accountsStorage));
        } catch (IOException ex) {
            Logger.getLogger(ApplicationImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        register(AccountManager.class, new AccountManagerImpl(accountStorageFactory));
        register(CredentialProvider.class, new CredentialProviderImpl());
    }
    
    private <T> void register(Class<T> serviceClass, T service) {
        services.put(serviceClass, service);
    }
    
    private void initServices() {
        IoC.bind(new HttpClientFactory(), HttpClient.class);
        IoC.bind(new RPC(), RPC.class);
        String accessTokenStorage = (String) config.get(ACCESS_TOKEN_STORAGE);
        DataStoreFactory accessTokenStorageFactory = null;
        try {
            accessTokenStorageFactory = new FileDataStoreFactory(new File(accessTokenStorage));
        } catch (IOException ex) {
            Logger.getLogger(ApplicationImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        IoC.bind(new AccessTokenProviderImpl(accessTokenStorageFactory), AccessTokenProvider.class);
        IoC.bind(new VKObjectFactory(), VKObjectFactory.class);
        
        JSONConverterRegistry converterRegistry = new JSONConverterRegistry()
            .register(Audio.class, new AudioJSONConverter());
        
        IoC.bind(converterRegistry, JSONConverterRegistry.class);
    }
    
}
