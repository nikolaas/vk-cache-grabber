package org.ns.vkcachegrabber.vk.impl;

import java.util.HashMap;
import java.util.Map;
import org.ns.vkcachegrabber.vk.model.Audio;
import org.ns.vkcachegrabber.vk.model.User;
import org.ns.vkcachegrabber.vk.model.VKObject;
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
    
    private final Map<Class<? extends VKObject>, Class<? extends VKObject>> instanceClasses;
    
    public VKObjectFactory() {
        instanceClasses = new HashMap<>();
    }
    
    public <K extends VKObject, V extends K> VKObjectFactory registerInstance(Class<K> iface, Class<V> impl) {
        instanceClasses.put(iface, impl);
        return this;
    }
    
    public <T extends VKObject> T create(Class<T> vkObjectClass) {
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
}
