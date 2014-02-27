package org.ns.vkcachegrabber.vk.json;

import org.ns.reflect.TypeConverter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONObject;
import org.ns.ioc.IoC;
import org.ns.pojo.PojoClass;
import org.ns.pojo.PojoManager;
import org.ns.pojo.PojoProperty;
import org.ns.util.Strings;
import org.ns.vkcachegrabber.vk.impl.VKObjectFactory;

/**
 *
 * @author stupak
 * @param <T>
 */
public class DefaultJSONConverter<T> implements JSONConverter<T> {

    private final Class<? extends T> implClass;
    private final PojoClass<? extends T> pojoClass;
    private final TypeConverter converter;
    private final Map<String, PojoProperty> mapping;

    public DefaultJSONConverter(Class<T> objectClass) {
        this.implClass = IoC.get(VKObjectFactory.class).getImpl(objectClass);
        this.pojoClass = PojoManager.getInstace().createPojoClass(implClass);
        this.converter = TypeConverter.defaultConverter();
        this.mapping = createMapping(pojoClass);
    }
    
    @Override
    public T convert(JSONObject json) throws JSONConvertException {
        T object = null;
        try {
            object = implClass.newInstance();
            Iterator jsonPropIterator = json.keySet().iterator();
            while ( jsonPropIterator.hasNext() ) {
                String jsonProp = (String) jsonPropIterator.next();
                if ( mapping.containsKey(jsonProp) ) {
                    PojoProperty pojoProp = mapping.get(jsonProp);
                    Object value = json.get(jsonProp);
                    Object converted = converter.convert(pojoProp.getType(), value);
                    pojoProp.setValue(object, converted);
                }
            }
        } catch (InstantiationException | IllegalAccessException | RuntimeException ex) {
            throw new JSONConvertException(ex);
        }
        return object;
    }
    
    private static Map<String, PojoProperty> createMapping(PojoClass<?> pojoClass) {
        Map<String, PojoProperty> mapping = new HashMap<>();
        for ( PojoProperty pojoProp : pojoClass.getProperties() ) {
            JSONMapping jsonMapping = pojoProp.getAnnotation(JSONMapping.class);
            String jsonName = null;
            if ( jsonMapping != null ) {
                jsonName = jsonMapping.name();
            }
            if ( Strings.empty(jsonName) ) {
                jsonName = JSONUtils.toJsonNotation(pojoProp.getName());
            }
            mapping.put(jsonName, pojoProp);
        }
        return mapping;
    }
}
