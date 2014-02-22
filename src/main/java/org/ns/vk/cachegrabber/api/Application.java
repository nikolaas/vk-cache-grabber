package org.ns.vk.cachegrabber.api;

import java.awt.Container;
import java.awt.Window;
import org.ns.vk.cachegrabber.api.vk.VKApi;

/**
 *
 * @author stupak
 */
public interface Application {

    public static final String ACCOUNT_STORAGE = "accountStorage";
    
    Window getMainWindow();
    
    Container getContentPane();
    
    VKApi getVKApi();
    
    String getVkClientId();
    
    <T> T getService(Class<T> serviceClass);
}
