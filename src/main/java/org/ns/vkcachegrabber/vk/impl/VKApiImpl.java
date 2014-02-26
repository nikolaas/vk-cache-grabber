package org.ns.vkcachegrabber.vk.impl;

import org.ns.ioc.IoC;
import org.ns.vkcachegrabber.vk.AuthService;
import org.ns.vkcachegrabber.vk.AccessToken;
import org.ns.vkcachegrabber.vk.model.Audio;
import org.ns.vkcachegrabber.vk.model.User;
import org.ns.vkcachegrabber.vk.json.JSONConverterRegistry;
import org.ns.vkcachegrabber.vk.VKApi;
import org.ns.vkcachegrabber.vk.VKMethod;
import org.ns.vkcachegrabber.vk.model.VKObject;
import org.ns.vkcachegrabber.vk.VkAuthException;
import org.ns.vkcachegrabber.vk.VkException;
import org.ns.vkcachegrabber.vk.json.JSONConverter;

/**
 *
 * @author stupak
 */
public class VKApiImpl implements VKApi {

    @Override
    public Audio getAudioById(String ownerId, String audioId) throws VkException {
        VKMethod m = VKMethod.apiMethodTemplate()
                .vkMethod(VKMethod.M.Audio.GET_BY_ID)
                .param(VKMethod.VK_AUDIOS, ownerId + "_" + audioId)
                .build();
        
        return doMethod(m, Audio.class);
    }

    @Override
    public User getUser(String userId) throws VkException {
        VKMethod m = VKMethod.apiMethodTemplate()
                .vkMethod(VKMethod.M.Users.GET)
                .param(VKMethod.USER_IDS, userId)
                .build();
        
        return doMethod(m, User.class);
    }

    private <T extends VKObject> T doMethod(VKMethod method, Class<T> resultType) throws VkException {
        JSONConverter<T> converter = IoC.get(JSONConverterRegistry.class).getConverter(resultType);
        
        RPC.Result<T> result = putAccessTokenAndExecute(method, converter);
        if ( result == null ) {
            return null;
        }
        if ( result.isError() ) {
            throw new VkException("Error occured when vkObject " + resultType.getName() + " loading", result.getError());
        } else {
            return result.getResult();
        }
    }
    
    private <T> RPC.Result<T> putAccessTokenAndExecute(VKMethod m, JSONConverter<T> converter) {
        Throwable authorizeException = null;
        AccessToken accessToken = null;
        try {
            accessToken = IoC.get(AuthService.class).getAccessToken();
        } catch (VkAuthException ex) {
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
