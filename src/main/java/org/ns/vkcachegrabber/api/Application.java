package org.ns.vkcachegrabber.api;

import java.awt.Container;
import java.awt.Window;
import org.ns.func.Callback;
import org.ns.vkcachegrabber.Config;
import org.ns.vkcachegrabber.vk.VKApi;

/**
 *
 * @author stupak
 */
public interface Application {

    String getName();
    
    Window getMainWindow();
    
    Container getContentPane();
    
    DocumentManager getDocumentManager();
    
    Config getConfig();
    
    VKApi getVKApi();
    
    String getVkClientId();
    
    <T> T getService(Class<T> serviceClass);
    
    void close();
    
    void addCloseHandler(Callback<Application> closeHandler);
}
