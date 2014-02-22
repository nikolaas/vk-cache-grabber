package org.ns.vk.cachegrabber.api.vk;

import org.ns.vk.cachegrabber.api.vk.VKObject;

/**
 *
 * @author stupak
 */
public class VKObjectFactory {
    
    public <T extends VKObject> T create(Class<T> vkObjectClass) {
        T instance = null;
        try {
            instance = vkObjectClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException("Can't create vkObject of type " + vkObjectClass.getName(), ex);
        }
        return instance;
    }
}
