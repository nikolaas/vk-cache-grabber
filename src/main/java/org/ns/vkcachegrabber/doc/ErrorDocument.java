package org.ns.vkcachegrabber.doc;

import javax.swing.JComponent;
import javax.swing.JPanel;
import org.ns.vkcachegrabber.ui.AbstractDocument;

/**
 *
 * @author stupak
 */
public class ErrorDocument extends AbstractDocument {

    private final JComponent errorPane;

    public ErrorDocument() {
        this.errorPane = new JPanel();
    }

    @Override
    protected void processOpenableChanged() {
    }

    @Override
    public JComponent getComponent() {
        return errorPane;
    }

}
