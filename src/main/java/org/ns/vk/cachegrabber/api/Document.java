package org.ns.vk.cachegrabber.api;

import java.net.URI;
import javax.swing.JComponent;
import org.ns.util.Closeable;

/**
 *
 * @author stupak
 */
public interface Document extends Closeable {

    URI getUri();
    
    JComponent getComponent();
    
    
}
