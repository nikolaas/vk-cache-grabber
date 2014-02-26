package org.ns.vkcachegrabber;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author stupak
 */
class Lifecycle implements WindowListener {

    private final ApplicationImpl application;

    Lifecycle(ApplicationImpl application) {
        this.application = application;
    }
    
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        application.close();
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
    
}
