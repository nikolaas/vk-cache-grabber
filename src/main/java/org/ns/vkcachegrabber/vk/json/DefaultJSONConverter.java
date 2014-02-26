package org.ns.vkcachegrabber.vk.json;

import org.json.simple.JSONObject;

/**
 *
 * @author stupak
 * @param <T>
 */
public class DefaultJSONConverter<T> implements JSONConverter<T> {

    private final Class<T> objectClass;

    public DefaultJSONConverter(Class<T> objectClass) {
        this.objectClass = objectClass;
    }
    
    @Override
    public T convert(JSONObject json) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
