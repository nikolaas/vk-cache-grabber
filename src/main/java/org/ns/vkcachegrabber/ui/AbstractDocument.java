package org.ns.vkcachegrabber.ui;

import com.sun.org.apache.xerces.internal.utils.Objects;
import javax.swing.JComponent;
import org.ns.vkcachegrabber.api.Document;
import org.ns.vkcachegrabber.api.Openable;

/**
 *
 * @author stupak
 */
public abstract class AbstractDocument implements Document {

    protected Openable openable;
    
    @Override
    public Openable getOpenable() {
        return openable;
    }

    @Override
    public void setOpenable(Openable openable) {
        if ( Objects.equals(this.openable, openable) ) {
            return;
        }
        this.openable = openable;
        processOpenableChanged();
    }
    
    protected abstract void processOpenableChanged();

    @Override
    public abstract JComponent getComponent();

    @Override
    public abstract void close();
    
}
