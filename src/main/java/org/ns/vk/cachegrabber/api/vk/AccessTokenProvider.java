package org.ns.vk.cachegrabber.api.vk;

/**
 *
 * @author stupak
 */
public interface AccessTokenProvider {

    /**
     * может быть null если пользователь отменил процесс авторизации
     * @return
     * @throws VkException 
     */
    AccessToken getAccessToken() throws VkException;
}
