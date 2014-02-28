package org.ns.vkcachegrabber;

import org.ns.vkcachegrabber.vk.impl.Autorizator;
import org.ns.vkcachegrabber.vk.impl.CredentialProviderImpl;
import java.awt.Container;
import org.ns.vkcachegrabber.vk.impl.VKApiImpl;
import java.awt.Window;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.http.client.HttpClient;
import org.ns.ioc.IoC;
import org.ns.vkcachegrabber.vk.AuthService;
import org.ns.vkcachegrabber.api.Application;
import org.ns.vkcachegrabber.api.DocumentManager;
import org.ns.vkcachegrabber.vk.VKApi;
import org.ns.vkcachegrabber.vk.impl.VKObjectFactory;
import org.ns.vkcachegrabber.vk.impl.RPC;
import org.ns.vkcachegrabber.api.OpenableHandlerRegistry;
import org.ns.vkcachegrabber.doc.AuthHandler;
import org.ns.vkcachegrabber.doc.ErrorHandler;
import org.ns.vkcachegrabber.vk.convert.ConverterRegistry;
import org.ns.store.FileDataStoreFactory;
import org.ns.task.TaskExecutionService;
import org.ns.task.TaskExecutionServiceImpl;
import org.ns.util.Closeable;
import org.ns.util.Utils;
import org.ns.vkcachegrabber.doc.CacheHandler;
import org.ns.vkcachegrabber.vk.convert.json.JSONUtils;

/**
 *
 * @author stupak
 */
class ApplicationImpl implements Application {

    private static final String VK_CLIENT_ID = "4205019";
    
    private final Window mainWindow;
    private final Container contentPane;
    private final Map<Class<?>, Object> services;
    private Config config;
    private final List<Closeable> closeables = new ArrayList<>();
    private final Lifecycle lifecycle;

    public ApplicationImpl(JFrame mainWindow) {
        this.lifecycle = new Lifecycle(this);
        this.mainWindow = mainWindow;
        this.mainWindow.addWindowListener(lifecycle);
        this.contentPane = mainWindow.getContentPane();
        this.services = new HashMap<>();
    }

    @Override
    public String getName() {
        return "vk-cache-grabber";
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
    public Config getConfig() {
        return config;
    }

    @Override
    public DocumentManager getDocumentManager() {
        return getService(DocumentManager.class);
    }
    
    public void setDocumentManager(DocumentManager documentManager) {
        register(DocumentManager.class, documentManager);
    }
    
    @Override
    public VKApi getVKApi() {
        return getService(VKApi.class);
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
        config = new Config(this);
        closeables.add(config);
        closeables.add(IoC.bind(TaskExecutionServiceImpl.getDefault(), TaskExecutionService.class));
        register(VKApi.class, new VKApiImpl());
        OpenableHandlerRegistry uriHandlerRegistry = new OpenableHandlerRegistryImpl()
            .register(new CacheHandler())
            .register(new AuthHandler())
            .register(new ErrorHandler());
        register(OpenableHandlerRegistry.class, uriHandlerRegistry);
        
        closeables.add(IoC.bind(new HttpClientFactory(), HttpClient.class));
        String configFolder = config.getAppConfigFolder();
        String accountsStorage = configFolder + ACCOUNT_STORAGE;
        String accessTokenStorage = configFolder + ACCESS_TOKEN_STORAGE;
        Autorizator.Builder builder = new Autorizator.Builder()
                .application(this)
                .setAccountStorageFactory(FileDataStoreFactory.createFrom(accountsStorage))
                .setAccessTokenStorageFactory(FileDataStoreFactory.createFrom(accessTokenStorage))
                .setCredentialProvider(new CredentialProviderImpl());
        closeables.add(IoC.bind(builder.build(), AuthService.class));
        closeables.add(IoC.bind(VKObjectFactory.defaultFactory(), VKObjectFactory.class));
        
        closeables.add(IoC.bind(JSONUtils.addVkJSONSupport(new ConverterRegistry()), ConverterRegistry.class));
        closeables.add(IoC.bind(JSONUtils.addVkJSONSupport(new RPC()), RPC.class));
        
        SwingUtilities.invokeLater(new GuiInitializer(this));
    }
    
    private <T> void register(Class<T> serviceClass, T service) {
        services.put(serviceClass, service);
    }
    
    @Override
    public void close() {
        for ( Closeable closeable : closeables ) {
            Utils.closeSilent(closeable);
        }
    }
}
