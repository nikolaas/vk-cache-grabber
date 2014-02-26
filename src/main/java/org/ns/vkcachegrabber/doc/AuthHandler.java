package org.ns.vkcachegrabber.doc;

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
public class AuthHandler implements OpenableHandler {

    public static final String OPENABLE_TYPE = "auth";
    public static final String LOGIN = "login";
    
    @Override
    public Collection<String> getSupportedOpenableTypes() {
        return Collections.singletonList(OPENABLE_TYPE);
    }

    @Override
    public Document open(Openable openable, OpenContext openContext) throws Exception {
        AuthDocument doc = new AuthDocument();
        doc.setOpenable(openable);
        return doc;
    }
    
}
