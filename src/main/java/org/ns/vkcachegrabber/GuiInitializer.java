package org.ns.vkcachegrabber;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.ns.event.Listener;
import org.ns.func.Callback;
import org.ns.ioc.IoC;
import org.ns.task.Task;
import org.ns.vkcachegrabber.vk.AuthService;
import org.ns.vkcachegrabber.vk.VkAuthException;
import org.ns.task.TaskExecutionService;
import org.ns.task.TaskUtils;
import org.ns.util.Utils;
import org.ns.vkcachegrabber.api.OpenContext;
import org.ns.vkcachegrabber.api.Openable;
import org.ns.vkcachegrabber.api.OpenableHandlerRegistry;
import org.ns.vkcachegrabber.api.OpenableHandlerRegistry.BeforeOpenEvent;
import org.ns.vkcachegrabber.doc.CacheHandler;
import org.ns.vkcachegrabber.ui.BrandingPane;
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
        
        if ( mainWindow instanceof JFrame ) {
            ((JFrame) mainWindow).setContentPane(new BrandingPane());
            mainMenu.setVisible(false);
            application.getService(OpenableHandlerRegistry.class)
                .addBeforeOpenEventListener(new BrandingCloseTask((JFrame) mainWindow, mainMenu));
        }
        
        IoC.get(TaskExecutionService.class).execute(TaskUtils.newTask("authorize", new Runnable() {

            @Override
            public void run() {
                try {
                    IoC.get(AuthService.class).authorize();
                } catch (VkAuthException ex) {
                    Logger.getLogger(GuiInitializer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        })).onFinish(new Callback<Task>() {

            @Override
            public void call(Task arg) {
                Utils.invokeWhenUIReady(new OpenCacheTask());
            }
        });
    }
    
    private class OpenCacheTask implements Runnable {

        @Override
        public void run() {
            String cachePath = application.getConfig().get("cachePath");
            Openable o = Openables.builder()
                    .openableType(CacheHandler.OPENABLE_TYPE)
                    .addParam(CacheHandler.CACHE_PATH, cachePath)
                    .build();
            application.getDocumentManager().open(o, new OpenContext());
        }
        
    }
    
    private class BrandingCloseTask implements Listener<BeforeOpenEvent> {

        private final JFrame mainWindow;
        private final MainMenu mainMenu;

        public BrandingCloseTask(JFrame mainWindow, MainMenu mainMenu) {
            this.mainWindow = mainWindow;
            this.mainMenu = mainMenu;
        }
        
        @Override
        public void listen(BeforeOpenEvent event) {
            application.getService(OpenableHandlerRegistry.class).removeBeforeOpenEventListener(this);
            mainWindow.setContentPane(application.getContentPane());
            mainMenu.setVisible(true);
        }

    }
}
