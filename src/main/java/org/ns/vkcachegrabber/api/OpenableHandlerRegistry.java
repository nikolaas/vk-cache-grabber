package org.ns.vkcachegrabber.api;

import org.ns.event.Event;
import org.ns.event.Listener;

/**
 *
 * @author stupak
 */
public interface OpenableHandlerRegistry extends OpenableHandler {
    
    OpenableHandlerRegistry register(OpenableHandler openableHandler);
    void unregister(OpenableHandler openableHandler);
    
    boolean addBeforeOpenEventListener(Listener<BeforeOpenEvent> l);
    void removeBeforeOpenEventListener(Listener<BeforeOpenEvent> l);
    
    public static class BeforeOpenEvent extends Event<OpenableHandlerRegistry> {

        private final  Openable openable;
        public BeforeOpenEvent(OpenableHandlerRegistry source, Openable openable) {
            super(source);
            this.openable = openable;
        }

        public Openable getOpenable() {
            return openable;
        }
        
    }
}
