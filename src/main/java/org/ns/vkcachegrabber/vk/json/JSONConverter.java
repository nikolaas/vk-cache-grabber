package org.ns.vkcachegrabber.vk.json;

import org.json.simple.JSONObject;

/**
 *
 * @author stupak
 * @param <T>
 */
public interface JSONConverter<T> {
    T convert(JSONObject json) throws JSONConvertException;
}
