package org.ns.vk.cachegrabber;

import javax.swing.JFrame;
import org.ns.ioc.IoC;
import org.ns.vk.cachegrabber.api.Application;

/**
 * 
 * @author stupak
 */
public class Startup {
    
    public static void main( String[] args ) {
        JFrame mainWindow = new JFrame();
        ApplicationImpl application = new ApplicationImpl(mainWindow);
        IoC.bind(application, Application.class);
        application.start();
    }
}
