package org.ns.vkcachegrabber;

import javax.swing.JFrame;
import org.ns.ioc.IoC;
import org.ns.vkcachegrabber.api.Application;

/**
 * 
 * @author stupak
 */
public class Startup {
    
    public static void main( String[] args ) {
        JFrame mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ApplicationImpl application = new ApplicationImpl(mainWindow);
        IoC.bind(application, Application.class);
        application.start();
    }
}