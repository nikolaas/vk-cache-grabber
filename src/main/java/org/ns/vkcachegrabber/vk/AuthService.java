package org.ns.vkcachegrabber.vk;

import org.ns.vkcachegrabber.api.Account;

/**
 *
 * @author stupak
 */
public interface AuthService {
    void authorize() throws VkAuthException;
    void reauthorize() throws VkAuthException;
    Account getCurrentAccount();
    AccessToken getAccessToken() throws VkAuthException;
}
