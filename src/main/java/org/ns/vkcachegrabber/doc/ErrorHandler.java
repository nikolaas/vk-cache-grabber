package org.ns.vkcachegrabber.doc;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import org.ns.vkcachegrabber.api.Document;
import org.ns.vkcachegrabber.api.OpenContext;
import org.ns.vkcachegrabber.api.Openable;
import org.ns.vkcachegrabber.api.OpenableHandler;

/**
 *
 * @author stupak
 */
public class ErrorHandler implements OpenableHandler {

    public static final String OPENABLE_TYPE = "error";
    
    @Override
    public Collection<String> getSupportedOpenableTypes() {
        return Collections.singletonList(OPENABLE_TYPE);
    }

    @Override
    public Document open(Openable openable, OpenContext context) throws Exception {
        Document doc = new ErrorDocument();
        doc.setOpenable(openable);
        return doc;
    }
    
}
