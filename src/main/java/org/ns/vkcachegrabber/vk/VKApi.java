package org.ns.vkcachegrabber.vk;

import org.ns.vkcachegrabber.vk.model.User;
import org.ns.vkcachegrabber.vk.model.Audio;

/**
 *
 * @author stupak
 */
public interface VKApi {
    
    Audio getAudioById(String ownerId, String audioId) throws VkException;
    
    User getUser(String userId) throws VkException;
}
