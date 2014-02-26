package org.ns.vkcachegrabber.ui;

import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.ns.ioc.IoC;
import org.ns.util.Utils;
import org.ns.vkcachegrabber.api.Application;
import org.ns.vkcachegrabber.api.Document;
import org.ns.vkcachegrabber.api.DocumentManager;
import org.ns.vkcachegrabber.api.OpenContext;
import org.ns.vkcachegrabber.api.Openable;
import org.ns.vkcachegrabber.api.OpenableHandlerRegistry;

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
    public void open(Openable openable, OpenContext openContext) {
        closeDocument();
        Document doc = null;
        try {
            doc = IoC.get(Application.class).getService(OpenableHandlerRegistry.class).open(openable, openContext);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error occured when open " + openable, ex);
        }
        if ( doc != null ) {
            this.current = doc;
            setDocumentAsContent(this.current);
        }
    }

    @Override
    public void openForResult(Openable openable, OpenContext openContext, ResultReceiver receiver) {
        closeDocument();
        Document doc = null;
        try {
            doc = IoC.get(Application.class).getService(OpenableHandlerRegistry.class).open(openable, openContext);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error occured when open " + openable, ex);
        }
        if ( doc == null && !(doc instanceof ResultableDocument) ) {
            throw new RuntimeException("Can't open " + openable + " for result");
        }
        if ( doc != null ) {
            ResultableDocument rd = (ResultableDocument) doc;
            rd.setResultListener(new ResultListener(receiver));
            this.current = doc;
            setDocumentAsContent(this.current);
        }
    }
    
    @Override
    public void closeCurrent() {
        closeDocument();
    }
    
    private void closeDocument() {
        if ( current != null) {
            contentPane.setViewportView(null);
            Throwable th = Utils.closeSilent(current);
            if ( th != null ) {
                logger.log(Level.SEVERE, "Error occured when document closing", th);
            }
        }
    }
    
    private void setDocumentAsContent(Document doc) {
        JComponent component = doc.getComponent();
        contentPane.setViewportView(component);
    }

    static class ResultListener {
        private final ResultReceiver receiver;

        public ResultListener(ResultReceiver receiver) {
            this.receiver = receiver;
        }
        
        void onResult(Object result, int statusCode) {
            receiver.onResult(statusCode, result);
        }
    }
}
