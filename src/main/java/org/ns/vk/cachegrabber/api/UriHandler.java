package org.ns.vk.cachegrabber.api;

import java.net.URI;
import java.util.Collection;

/**
 *
 * @author stupak
 */
public interface UriHandler {
    
    Collection<String> getSchemes();
    Document openUri(URI uri) throws Exception;
}
