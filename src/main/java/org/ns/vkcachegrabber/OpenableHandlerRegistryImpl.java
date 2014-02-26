package org.ns.vkcachegrabber;

import org.ns.vkcachegrabber.api.OpenableHandler;
import org.ns.vkcachegrabber.api.OpenableHandlerRegistry;
import org.ns.vkcachegrabber.api.Document;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ns.event.ListenHelper;
import org.ns.event.Listener;
import org.ns.vkcachegrabber.api.OpenContext;
import org.ns.vkcachegrabber.api.Openable;

/**
 *
 * @author stupak
 */
public class OpenableHandlerRegistryImpl implements OpenableHandlerRegistry {

    private static final Logger logger = Logger.getLogger(OpenableHandlerRegistryImpl.class.getName());
    private final Lock lock = new ReentrantLock();
    private final ConcurrentMap<String, OpenableHandler> handlers;
    private final ListenHelper<BeforeOpenEvent> listenHelper;

    public OpenableHandlerRegistryImpl() {
        this.handlers = new ConcurrentHashMap<>();
        this.listenHelper = new ListenHelper<>();
    }
    
    @Override
    public OpenableHandlerRegistry register(OpenableHandler openableHandler) {
        lock.lock();
        try {
            for ( String type : openableHandler.getSupportedOpenableTypes()) {
                if ( handlers.containsKey(type) ) {
                    throw new RuntimeException("OpenableHandler for openable's type " + type + " already registered");
                }
                handlers.put(type, openableHandler);
            }
        } finally {
            lock.unlock();
        }
        return this;
    }

    @Override
    public void unregister(OpenableHandler openableHandler) {
        lock.lock();
        try {
            for ( String type : openableHandler.getSupportedOpenableTypes()) {
                if ( handlers.containsKey(type) ) {
                    OpenableHandler h = handlers.get(type);
                    if ( openableHandler == h ) {
                        handlers.remove(type);
                    } else {
                        logger.log(Level.WARNING, "OpenableHandler not registered for opeable'a type {0}", type);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Collection<String> getSupportedOpenableTypes() {
        return Collections.unmodifiableCollection(handlers.keySet());
    }

    @Override
    public Document open(Openable openable, OpenContext openContext) throws Exception {
        OpenableHandler handler = findHandler(openable);
        fireBeforeOpenEvent(openable);
        return handler.open(openable, openContext);
    }
    
    private OpenableHandler findHandler(Openable openable) {
        String type = openable.getOpenableType();
        OpenableHandler handler = handlers.get(type);
        if ( handler == null ) {
            throw new NullPointerException("Unknown openable's type: " + type);
        }
        return handler;
    }

    private void fireBeforeOpenEvent(Openable openable) {
        listenHelper.listen(new BeforeOpenEvent(this, openable));
    }
    
    @Override
    public boolean addBeforeOpenEventListener(Listener<BeforeOpenEvent> l) {
        return listenHelper.add(l);
    }

    @Override
    public void removeBeforeOpenEventListener(Listener<BeforeOpenEvent> l) {
        listenHelper.remove(l);
    }
    
}
