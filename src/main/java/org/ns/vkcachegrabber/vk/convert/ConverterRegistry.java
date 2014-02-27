package org.ns.vkcachegrabber.vk.convert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ns.vkcachegrabber.vk.model.VKObject;

/**
 *
 * @author stupak
 */
public class ConverterRegistry {

    private final Map<String, TypedConverters> registry = new HashMap<>();
    
    public <V extends VKObject> ConverterRegistry register(String type, Class<V> vkObjectClass, Converter<?, V> converter) {
        TypedConverters converters = registry.get(type);
        if ( converters == null ) {
            converters = new TypedConverters();
            registry.put(type, converters);
        }
        converters.put(vkObjectClass, converter);
        return this;
    }
    
    public <K, T extends VKObject> Converter<K, T> getConverter(String type, Class<T> vkObjectClass) {
        TypedConverters converters = registry.get(type);
        if ( converters == null ) {
            return null;
        }
        return (Converter<K, T>) converters.get(vkObjectClass);
    }
    
    public <T, K extends Iterable<T>, V extends VKObject> Converter<K, List<V>> getListConverter(String type, Class<V> vkObjectClass) {
        Converter<T, V> itemConverter = getConverter(type, vkObjectClass);
        return wrap(itemConverter);
    }
    
    private <T, K extends Iterable<T>, V extends VKObject> Converter<K, List<V>> wrap(Converter<T, V> converter) {
        if ( converter == null ) {
            return null;
        }
        return new ListConverter<>(converter);
    }
    
    private static class TypedConverters extends HashMap<Class<? extends VKObject>, Converter<?, ? extends VKObject>> {
    }
}
