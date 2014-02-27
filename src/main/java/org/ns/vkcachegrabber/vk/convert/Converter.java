package org.ns.vkcachegrabber.vk.convert;

import org.ns.vkcachegrabber.vk.convert.json.JSONConvertException;

/**
 *
 * @author stupak
 * @param <K>
 * @param <V>
 */
public interface Converter<K, V> {
     V convert(K from) throws JSONConvertException;
}
