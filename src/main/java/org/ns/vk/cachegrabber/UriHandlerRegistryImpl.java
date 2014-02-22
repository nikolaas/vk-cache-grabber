package org.ns.vk.cachegrabber;

import org.ns.vk.cachegrabber.api.UriHandler;
import org.ns.vk.cachegrabber.api.UriHandlerRegistry;
import org.ns.vk.cachegrabber.api.Document;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stupak
 */
public class UriHandlerRegistryImpl implements UriHandlerRegistry {

    private static final Logger logger = Logger.getLogger(UriHandlerRegistryImpl.class.getName());
    private final Lock lock = new ReentrantLock();
    private final ConcurrentMap<String, UriHandler> handlers;

    public UriHandlerRegistryImpl() {
        this.handlers = new ConcurrentHashMap<>();
    }
    
    @Override
    public UriHandlerRegistry register(UriHandler uriHandler) {
        lock.lock();
        try {
            for ( String scheme : uriHandler.getSchemes() ) {
                if ( handlers.containsKey(scheme) ) {
                    throw new RuntimeException("UriHandler for uri with scheme " + scheme + " already registered");
                }
                handlers.put(scheme, uriHandler);
            }
        } finally {
            lock.unlock();
        }
        return this;
    }

    @Override
    public void unregister(UriHandler uriHandler) {
        lock.lock();
        try {
            for ( String scheme : uriHandler.getSchemes() ) {
                if ( handlers.containsKey(scheme) ) {
                    UriHandler h = handlers.get(scheme);
                    if ( uriHandler == h ) {
                        handlers.remove(scheme);
                    } else {
                        logger.log(Level.WARNING, "UriHandler not registered for scheme {0}", scheme);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Collection<String> getSchemes() {
        return Collections.unmodifiableCollection(handlers.keySet());
    }

    @Override
    public Document openUri(URI uri) throws Exception {
        return findHandler(uri).openUri(uri);
    }
    
    private UriHandler findHandler(URI uri) {
        String scheme = uri.getScheme();
        UriHandler handler = handlers.get(scheme);
        if ( handler == null ) {
            throw new NullPointerException("Unknown scheme: " + scheme);
        }
        return handler;
    }
    
}
