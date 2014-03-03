package org.ns.vkcachegrabber;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import org.ns.func.Callback;
import org.ns.vkcachegrabber.api.Application;

/**
 *
 * @author stupak
 */
class Lifecycle implements WindowListener {

    private final ApplicationImpl application;
    private final List<Callback<Application>> closeHandlers;

    Lifecycle(ApplicationImpl application) {
        this.application = application;
        this.closeHandlers = new ArrayList<>();
    }
    
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        notifyCloseHandlers();
        application.close();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
    
    private void notifyCloseHandlers() {
        for ( Callback<Application> closeHandler : closeHandlers ) {
            closeHandler.call(application);
        }
    }
    
    public void addCloseHandler(Callback<Application> closeHandler) {
        closeHandlers.add(closeHandler);
    }
}
