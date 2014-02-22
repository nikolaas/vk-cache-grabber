package org.ns.vk.cachegrabber.api;

import java.awt.Container;
import java.awt.Window;
import org.ns.vk.cachegrabber.api.vk.VKApi;

/**
 *
 * @author stupak
 */
public interface Application {

    Window getMainWindow();
    
    Container getContentPane();
    
    VKApi getVKApi();
    
    String getVkClientId();
    
    String getVkApiSecret();
}
