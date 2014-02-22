package org.ns.vk.cachegrabber.json;

import org.json.simple.JSONObject;

/**
 *
 * @author stupak
 */
public interface JSONConverter<T> {
    T convert(JSONObject json);
}
