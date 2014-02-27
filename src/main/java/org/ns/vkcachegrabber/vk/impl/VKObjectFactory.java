package org.ns.vkcachegrabber.vk.impl;

import java.util.HashMap;
import java.util.Map;
import org.ns.vkcachegrabber.vk.model.Audio;
import org.ns.vkcachegrabber.vk.model.User;
import org.ns.vkcachegrabber.vk.model.impl.AudioImpl;
import org.ns.vkcachegrabber.vk.model.impl.UserImpl;

/**
 *
 * @author stupak
 */
public class VKObjectFactory {

    public static VKObjectFactory defaultFactory() {
        VKObjectFactory def = new VKObjectFactory();
        def
            .registerInstance(Audio.class, AudioImpl.class)
            .registerInstance(User.class, UserImpl.class);
        return def;
    }
    
    private final Map<Class<?>, Class<?>> instanceClasses;
    
    public VKObjectFactory() {
        instanceClasses = new HashMap<>();
    }
    
    public <K, V> VKObjectFactory registerInstance(Class<K> iface, Class<V> impl) {
        instanceClasses.put(iface, impl);
        return this;
    }
    
    public <T> T create(Class<T> vkObjectClass) {
        T instance = null;
        Class<? extends T> impl = (Class<? extends T>) instanceClasses.get(vkObjectClass);
        if ( impl != null ) {
            try {
                instance = impl.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException("Can't create vkObject of type " + vkObjectClass.getName(), ex);
            }
        }
        return instance;
    }
    
    public <T> Class<? extends T> getImpl(Class<T> iface) {
        return (Class<? extends T>) instanceClasses.get(iface);
    }
}
