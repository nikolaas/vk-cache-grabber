package org.ns.vk.cachegrabber.api.vk.impl;

import org.ns.ioc.IoC;
import org.ns.vk.cachegrabber.api.vk.AccessToken;
import org.ns.vk.cachegrabber.api.vk.AccessTokenProvider;
import org.ns.vk.cachegrabber.api.vk.Audio;
import org.ns.vk.cachegrabber.json.JSONConverterRegistry;
import org.ns.vk.cachegrabber.api.vk.VKApi;
import org.ns.vk.cachegrabber.api.vk.VKMethod;
import org.ns.vk.cachegrabber.api.vk.VkException;
import org.ns.vk.cachegrabber.json.JSONConverter;

/**
 *
 * @author stupak
 */
public class VKApiImpl implements VKApi {

    @Override
    public Audio getById(String ownerId, String audioId) throws VkException {
        VKMethod m = VKMethod.apiMethodTemplate()
                .vkMethod(VKMethod.M.Audio.GET_BY_ID)
                .param(VKMethod.VK_AUDIOS, ownerId + "_" + audioId)
                .build();
        
        
        JSONConverter<Audio> converter = IoC.get(JSONConverterRegistry.class).getConverter(Audio.class);
        RPC.Result<Audio> result = putAccessTokenAndExecute(m, converter);
        if ( result == null ) {
            return null;
        }
        if ( result.isError() ) {
            throw new VkException("Error occured when audio loading", result.getError());
        } else {
            return result.getResult();
        }
    }

    private <T> RPC.Result<T> putAccessTokenAndExecute(VKMethod m, JSONConverter<T> converter) {
        Throwable authorizeException = null;
        AccessToken accessToken = null;
        try {
            accessToken = IoC.get(AccessTokenProvider.class).getAccessToken();
        } catch (VkException ex) {
            authorizeException = ex;
        }
        if ( authorizeException != null ) {
            return new RPC.Result<>(authorizeException);
        }
        
        RPC.Result<T> result = null;
        if ( accessToken != null ) {
            m.setParam(VKMethod.VK_ACCESS_TOKEN, accessToken.getAccessToken());
            result = IoC.get(RPC.class).execute(m, new JSONResponceConverter(converter));
        }
        return result;
    }

}
