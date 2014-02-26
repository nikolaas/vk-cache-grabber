package org.ns.vkcachegrabber.api;

/**
 *
 * @author stupak
 */
public interface OpenableHandlerRegistry extends OpenableHandler {
    
    OpenableHandlerRegistry register(OpenableHandler openableHandler);
    void unregister(OpenableHandler openableHandler);
}
