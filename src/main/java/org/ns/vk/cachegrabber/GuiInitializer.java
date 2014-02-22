package org.ns.vk.cachegrabber;

import java.awt.BorderLayout;
import java.awt.Window;
import javax.swing.JFrame;
import org.ns.vk.cachegrabber.api.Application;
import org.ns.vk.cachegrabber.ui.MainMenu;
import org.ns.vk.cachegrabber.ui.MainPane;

/**
 *
 * @author stupak
 */
class GuiInitializer implements Runnable {
    
    private final Application application;

    public GuiInitializer(Application application) {
        this.application = application;
    }

    @Override
    public void run() {
        MainMenu mainMenu = new MainMenu();
        MainPane mainPane = new MainPane();
        
        Window mainWindow = application.getMainWindow();
        if ( mainWindow instanceof JFrame ) {
            JFrame mainFrame = (JFrame) mainWindow;
            mainFrame.setJMenuBar(mainMenu);
            application.getContentPane().setLayout(new BorderLayout());
            application.getContentPane().add(mainPane, BorderLayout.CENTER);
        } else {
            application.getContentPane().setLayout(new BorderLayout());
            application.getContentPane().add(mainMenu, BorderLayout.NORTH);
            application.getContentPane().add(mainPane, BorderLayout.CENTER);
        }
        mainWindow.setSize(300, 300);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }
}
