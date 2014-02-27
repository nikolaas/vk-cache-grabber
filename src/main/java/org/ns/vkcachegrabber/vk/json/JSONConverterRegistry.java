package org.ns.vkcachegrabber.vk.json;

import java.util.HashMap;
import java.util.Map;
import org.ns.vkcachegrabber.vk.model.VKObject;

/**
 *
 * @author stupak
 */
public class JSONConverterRegistry {

    private final Map<Class<? extends VKObject>, JSONConverter<? extends VKObject>> converters;

    public JSONConverterRegistry() {
        this.converters = new HashMap<>();
    }
    
    public <T extends VKObject> JSONConverterRegistry register(Class<T> vkObjectClass, JSONConverter<T> converter) {
        converters.put(vkObjectClass, converter);
        return this;
    }
    
    public <T extends VKObject> JSONConverter<T> getConverter(Class<T> vkObjectClass) {
        return (JSONConverter<T>) converters.get(vkObjectClass);
    }
}
