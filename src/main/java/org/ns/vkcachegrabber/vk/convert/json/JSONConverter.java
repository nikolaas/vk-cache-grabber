package org.ns.vkcachegrabber.vk.convert.json;

import org.json.simple.JSONAware;
import org.ns.vkcachegrabber.vk.convert.Converter;

/**
 *
 * @author stupak
 * @param <T>
 */
public interface JSONConverter<T> extends Converter<JSONAware, T> {
    
    @Override
    T convert(JSONAware json) throws JSONConvertException;
}
