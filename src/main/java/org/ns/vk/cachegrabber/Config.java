package org.ns.vk.cachegrabber;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author stupak
 */
//TODO реализовать
public class Config {

    private final Map<String, Object> prefs;
    
    public Config() {
        this.prefs = new HashMap<>();
    }

    public Object get(String key) {
        return prefs.get(key);
    }
}
