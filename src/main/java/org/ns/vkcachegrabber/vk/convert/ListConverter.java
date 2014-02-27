package org.ns.vkcachegrabber.vk.convert;

import org.ns.vkcachegrabber.vk.convert.json.JSONConvertException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.ns.vkcachegrabber.vk.model.VKObject;

/**
 *
 * @author stupak
 * @param <V>
 */
class ListConverter<T, K extends Iterable<T>, V extends VKObject> implements Converter<K, List<V>> {

    private final Converter<T, V> objectConverter;

    public ListConverter(Converter<T, V> objectConverter) {
        this.objectConverter = objectConverter;
    }
    
    @Override
    public List<V> convert(K from) throws JSONConvertException {
        List<V> list = new ArrayList<>();
        Iterator<T> iterator = from.iterator();
        while ( iterator.hasNext() ) {
            T item = iterator.next();
            V object = objectConverter.convert(item);
            list.add(object);
        }
        return list;
    }
    
}
