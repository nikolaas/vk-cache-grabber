package org.ns.vk.cachegrabber.ui;

import java.awt.BorderLayout;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.ns.ioc.IoC;
import org.ns.util.Utils;
import org.ns.vk.cachegrabber.api.Application;
import org.ns.vk.cachegrabber.api.Document;
import org.ns.vk.cachegrabber.api.DocumentManager;
import org.ns.vk.cachegrabber.api.UriHandlerRegistry;

/**
 *
 * @author stupak
 */
public class MainPane extends JPanel implements DocumentManager {

    private static final Logger logger =  Logger.getLogger(MainPane.class.getName());
    
    private Document current;
    private JScrollPane contentPane;
    
    public MainPane() {
        super(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        contentPane = new JScrollPane();
        add(contentPane, BorderLayout.CENTER);
    }
    
    @Override
    public void openUri(URI uri) {
        if ( current != null) {
            contentPane.setViewportView(null);
            Throwable th = Utils.closeSilent(current);
            if ( th != null ) {
                logger.log(Level.SEVERE, "Error occured when document closing", th);
            }
        }
        Document doc = null;
        try {
            doc = IoC.get(Application.class).getService(UriHandlerRegistry.class).openUri(uri);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error occured when open uri " + uri, ex);
        }
        if ( doc != null ) {
            this.current = doc;
            setDocumentAsContent(this.current);
        }
    }
    
    private void setDocumentAsContent(Document doc) {
        JComponent component = doc.getComponent();
        contentPane.setViewportView(component);
    }
    
}
