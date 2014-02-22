package org.ns.vk.cachegrabber;

import java.awt.Container;
import org.ns.vk.cachegrabber.api.vk.impl.AccessTokenProviderImpl;
import org.ns.vk.cachegrabber.api.vk.impl.VKApiImpl;
import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.http.client.HttpClient;
import org.ns.ioc.IoC;
import org.ns.vk.cachegrabber.api.Application;
import org.ns.vk.cachegrabber.api.vk.AccessTokenProvider;
import org.ns.vk.cachegrabber.api.vk.Audio;
import org.ns.vk.cachegrabber.api.vk.VKApi;
import org.ns.vk.cachegrabber.api.vk.VKObjectFactory;
import org.ns.vk.cachegrabber.api.vk.impl.RPC;
import org.ns.vk.cachegrabber.json.AudioJSONConverter;
import org.ns.vk.cachegrabber.json.JSONConverterRegistry;

/**
 *
 * @author stupak
 */
class ApplicationImpl implements Application {

    private static final String VK_CLIENT_ID = "4199664";
    private static final String VK_API_SECRET = "MBzZoHYM7O0BjrFf6J3a";
    
    private final Window mainWindow;
    private final Container contentPane;
    private final VKApi vkApi;

    public ApplicationImpl(JFrame mainWindow) {
        this.mainWindow = mainWindow;
        this.contentPane = mainWindow.getContentPane();
        this.vkApi = new VKApiImpl();
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
    public String getVkApiSecret() {
        return VK_API_SECRET;
    }
    
    void start() {
        initServices();
        SwingUtilities.invokeLater(new GuiInitializer(this));
    }
    
    private void initServices() {
        IoC.bind(new HttpClientFactory(), HttpClient.class);
        IoC.bind(new RPC(), RPC.class);
        IoC.bind(new AccessTokenProviderImpl(), AccessTokenProvider.class);
        IoC.bind(new VKObjectFactory(), VKObjectFactory.class);
        
        JSONConverterRegistry converterRegistry = new JSONConverterRegistry()
            .register(Audio.class, new AudioJSONConverter());
        
        IoC.bind(converterRegistry, JSONConverterRegistry.class);
    }
    
}
