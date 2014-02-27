package org.ns.vkcachegrabber.vk.impl;

import java.util.List;
import org.ns.func.Function;
import org.ns.ioc.IoC;
import org.ns.vkcachegrabber.vk.AuthService;
import org.ns.vkcachegrabber.vk.AccessToken;
import org.ns.vkcachegrabber.vk.model.Audio;
import org.ns.vkcachegrabber.vk.model.User;
import org.ns.vkcachegrabber.vk.VKApi;
import org.ns.vkcachegrabber.vk.VKMethod;
import org.ns.vkcachegrabber.vk.model.VKObject;
import org.ns.vkcachegrabber.vk.VkAuthException;
import org.ns.vkcachegrabber.vk.VkException;

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
        List<Audio> audios = getList(m, Audio.class);
        return audios.get(0);
    }

    @Override
    public User getUser(String userId) throws VkException {
        VKMethod m = VKMethod.apiMethodTemplate()
                .vkMethod(VKMethod.M.Users.GET)
                .param(VKMethod.USER_IDS, userId)
                .build();
        List<User> users = getList(m, User.class);
        return users.get(0);
    }

    /**
     * многие методы vk api возвращают результат в виде массива (json или xml),
     * поэтому нужно смотреть в документации, что именно метод возвращает, и в 
     * зависимости от этого использовать этот метод или {@link #getList(org.ns.vkcachegrabber.vk.VKMethod, java.lang.Class) }
     * @param <T>
     * @param method
     * @param resultType
     * @return
     * @throws VkException 
     */
    private <T extends VKObject> T getObject(VKMethod method, Class<T> resultType) throws VkException {
        RPC.Result<T> result = putAccessTokenAndExecute(method, new ObjectFunction<>(resultType));
        if ( result == null ) {
            return null;
        }
        if ( result.isError() ) {
            throw new VkException("Error occured when vkObject " + resultType.getName() + " loading", result.getError());
        } else {
            return result.getResult();
        }
    }
    
    /**
     * многие методы vk api возвращают результат в виде массива (json или xml),
     * поэтому нужно смотреть в документации, что именно метод возвращает, и в 
     * зависимости от этого использовать этот метод или {@link #getObject(org.ns.vkcachegrabber.vk.VKMethod, java.lang.Class) }
     * @param <T>
     * @param method
     * @param resultType
     * @return
     * @throws VkException 
     */
    private <T extends VKObject> List<T> getList(VKMethod method, Class<T> resultType) throws VkException {
        RPC.Result<List<T>> result = putAccessTokenAndExecute(method, new ListFunction<>(resultType));
        if ( result == null ) {
            return null;
        }
        if ( result.isError() ) {
            throw new VkException("Error occured when list vkObjects of " + resultType.getName() + " loading", result.getError());
        } else {
            return result.getResult();
        }
    }
    
    private <T> RPC.Result<T> putAccessTokenAndExecute(VKMethod method, Function<ParsedObject, T> function) {
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
            method.setParam(VKMethod.VK_ACCESS_TOKEN, accessToken.getAccessToken());
            result = IoC.get(RPC.class).execute(method, function);
        }
        return result;
    }

}
