package org.ns.vkcachegrabber.vk.impl;

import java.util.List;
import org.ns.func.Function;
import org.ns.ioc.IoC;
import org.ns.vkcachegrabber.vk.convert.Converter;
import org.ns.vkcachegrabber.vk.convert.ConverterRegistry;
import org.ns.vkcachegrabber.vk.model.VKObject;

/**
 *
 * @author stupak
 * @param <T>
 */
class ListFunction<T extends VKObject> implements Function<ParsedObject, List<T>> {

    private final Class<T> objectClass;

    public ListFunction(Class<T> objectClass) {
        this.objectClass = objectClass;
    }
    
    @Override
    public List<T> apply(ParsedObject key) throws Exception {
        ConverterRegistry converterRegistry = IoC.get(ConverterRegistry.class);
        Converter converter = converterRegistry.getListConverter(key.getType(), objectClass);
        if ( converter == null ) {
            throw new NullPointerException("Converting from " + key.getObject().getClass().getName() + " to " + objectClass.getName() + " not supported");
        }
        return (List<T>) converter.convert(key.getObject());
    }
    
}
