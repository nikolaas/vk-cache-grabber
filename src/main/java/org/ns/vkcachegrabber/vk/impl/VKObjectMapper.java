package org.ns.vkcachegrabber.vk.impl;

import org.json.simple.JSONObject;
import org.ns.vkcachegrabber.vk.model.VKObject;

/**
 *
 * @author stupak
 * @param <T>
 */
public interface VKObjectMapper<T extends VKObject> {

    void mapToObject(JSONObject json, T object);
}
