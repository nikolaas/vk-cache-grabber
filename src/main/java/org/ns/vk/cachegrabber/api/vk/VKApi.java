package org.ns.vk.cachegrabber.api.vk;

/**
 *
 * @author stupak
 */
public interface VKApi {
    
    Audio getById(String ownerId, String audioId) throws VkException;
    
}
