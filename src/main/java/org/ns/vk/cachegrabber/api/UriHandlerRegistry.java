package org.ns.vk.cachegrabber.api;

/**
 *
 * @author stupak
 */
public interface UriHandlerRegistry extends UriHandler {
    
    UriHandlerRegistry register(UriHandler uriHandler);
    
    void unregister(UriHandler uriHandler);
}
