package org.ns.vkcachegrabber;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.ns.ioc.IoC;
import org.ns.vkcachegrabber.vk.AuthService;
import org.ns.vkcachegrabber.vk.VkAuthException;
import org.ns.task.TaskExecutionService;
import org.ns.vkcachegrabber.ui.MainMenu;
import org.ns.vkcachegrabber.ui.MainPane;

/**
 *
 * @author stupak
 */
class GuiInitializer implements Runnable {
    
    private final ApplicationImpl application;

    public GuiInitializer(ApplicationImpl application) {
        this.application = application;
    }

    @Override
    public void run() {
        MainMenu mainMenu = new MainMenu();
        MainPane mainPane = new MainPane();
        application.setDocumentManager(mainPane);
        
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
        
        mainWindow.setPreferredSize(new Dimension(300, 300));
        mainWindow.setLocationRelativeTo(null);
        mainWindow.pack();
        mainWindow.setVisible(true);
        
        IoC.get(TaskExecutionService.class).execute("authorize", new Runnable() {

            @Override
            public void run() {
                try {
                    IoC.get(AuthService.class).authorize();
                } catch (VkAuthException ex) {
                    Logger.getLogger(GuiInitializer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
