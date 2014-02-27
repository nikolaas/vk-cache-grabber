package org.ns.vkcachegrabber.vk.impl;

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
class ObjectFunction<T extends VKObject> implements Function<ParsedObject, T> {

    private final Class<T> objectClass;

    public ObjectFunction(Class<T> objectClass) {
        this.objectClass = objectClass;
    }
    
    @Override
    public T apply(ParsedObject key) throws Exception {
        ConverterRegistry converterRegistry = IoC.get(ConverterRegistry.class);
        Converter converter = converterRegistry.getConverter(key.getType(), objectClass);
        if ( converter == null ) {
            throw new NullPointerException("Converting from " + key.getObject().getClass().getName() + " to " + objectClass.getName() + " not supported");
        }
        return (T) converter.convert(key.getObject());
    }
    
}
