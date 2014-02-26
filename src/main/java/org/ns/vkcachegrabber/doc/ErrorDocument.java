package org.ns.vkcachegrabber.doc;

import javax.swing.JComponent;
import javax.swing.JPanel;
import org.ns.vkcachegrabber.api.Document;
import org.ns.vkcachegrabber.api.Openable;

/**
 *
 * @author stupak
 */
public class ErrorDocument implements Document {

    private Openable openable;
    private final JComponent errorPane;

    public ErrorDocument() {
        this.errorPane = new JPanel();
    }

    @Override
    public Openable getOpenable() {
        return openable;
    }

    @Override
    public void setOpenable(Openable openable) {
        this.openable = openable;
    }

    @Override
    public JComponent getComponent() {
        return errorPane;
    }

    @Override
    public void close() {
    }
    
}
