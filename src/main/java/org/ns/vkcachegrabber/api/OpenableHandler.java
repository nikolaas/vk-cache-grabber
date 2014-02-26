package org.ns.vkcachegrabber.api;

import java.util.Collection;

/**
 *
 * @author stupak
 */
public interface OpenableHandler {
    
    Collection<String> getSupportedOpenableTypes();
    Document open(Openable openable, OpenContext context) throws Exception;
}
