package org.ns.vkcachegrabber.api;

import javax.swing.JComponent;
import org.ns.util.Closeable;

/**
 *
 * @author stupak
 */
public interface Document extends Closeable {

    Openable getOpenable();
    void setOpenable(Openable openable);
    
    JComponent getComponent();
    
}
