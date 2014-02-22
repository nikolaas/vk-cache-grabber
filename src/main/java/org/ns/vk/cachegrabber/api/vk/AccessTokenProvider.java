package org.ns.vk.cachegrabber.api.vk;

import org.ns.func.Callback;

/**
 *
 * @author stupak
 */
public interface AccessTokenProvider {

    void getAccessToken(Callback<AccessToken> callback);
}
