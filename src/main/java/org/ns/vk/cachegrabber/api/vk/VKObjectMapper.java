package org.ns.vk.cachegrabber.api.vk;

import org.json.simple.JSONObject;

/**
 *
 * @author stupak
 * @param <T>
 */
public interface VKObjectMapper<T extends VKObject> {

    void mapToObject(JSONObject json, T object);
}
