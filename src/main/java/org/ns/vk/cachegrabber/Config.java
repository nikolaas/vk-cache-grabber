package org.ns.vk.cachegrabber;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.ns.func.Function;
import org.ns.vk.cachegrabber.api.Application;

/**
 * 
 * @author stupak
 */
//TODO реализовать
public class Config {

    private final Application application;
    private final Map<String, Function<String, ? extends Object>> prefs;
    
    public Config(Application application) {
        this.application = application;
        this.prefs = new HashMap<>();
        prefs.put(Application.USER_PREF_FOLDER, new Function<String, Object>() {

            @Override
            public Object apply(String key) {
                return System.getProperty("user.home") + File.separator + "." + Config.this.application.getName();
            }
        });
        prefs.put(Application.ACCESS_TOKEN_STORAGE, new Function<String, Object>() {

            @Override
            public Object apply(String key) {
                return Application.ACCESS_TOKEN_STORAGE;
            }
        });
        prefs.put(Application.ACCOUNT_STORAGE, new Function<String, Object>() {

            @Override
            public Object apply(String key) {
                return Application.ACCOUNT_STORAGE;
            }
        });
    }

    public Object get(String key) {
        Function<String, ? extends Object> f = prefs.get(key);
        return f == null ? null : f.apply(key);
    }
}
