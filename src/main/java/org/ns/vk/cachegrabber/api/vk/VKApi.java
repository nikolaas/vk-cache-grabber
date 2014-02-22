package org.ns.vk.cachegrabber.api.vk;

import org.ns.func.Callback;

/**
 *
 * @author stupak
 */
public interface VKApi {
    
    void getById(String ownerId, String audioId, Callback<Audio> callback);
    
}
