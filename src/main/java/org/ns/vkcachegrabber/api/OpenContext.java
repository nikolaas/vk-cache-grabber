package org.ns.vkcachegrabber.api;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author stupak
 */
public class OpenContext {

    private final Map<String, Object> objects;

    public OpenContext() {
        this.objects = new HashMap<>();
    }
    
    public Object get(String key) {
        return objects.get(key);
    }
    
    public <T> T getAs(String key, Class<T> as) {
        Object obj = get(key);
        if ( obj == null ) {
            return null;
        }
        if ( as.isInstance(obj) ) {
            return as.cast(obj);
        } else {
            return null;
        }
    }
    
    public void set(String key, Object object) {
        objects.put(key, object);
    }
}
