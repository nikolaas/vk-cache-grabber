package org.ns.vk.cachegrabber.api.vk.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.ns.func.Callback;
import org.ns.func.Errorback;
import org.ns.ioc.IoC;
import org.ns.vk.cachegrabber.api.vk.AccessToken;
import org.ns.vk.cachegrabber.api.vk.AccessTokenProvider;
import org.ns.vk.cachegrabber.api.vk.Audio;
import org.ns.vk.cachegrabber.json.JSONConverterRegistry;
import org.ns.vk.cachegrabber.api.vk.VKApi;
import org.ns.vk.cachegrabber.api.vk.VKMethod;
import org.ns.vk.cachegrabber.json.JSONConverter;

/**
 *
 * @author stupak
 */
public class VKApiImpl implements VKApi {

    @Override
    public void getById(String ownerId, String audioId, final Callback<Audio> callback) {
        VKMethod m = VKMethod.apiMethodTemplate()
                .vkMethod(VKMethod.M.Audio.GET_BY_ID)
                .param(VKMethod.VK_AUDIOS, ownerId + "_" + audioId)
                .build();
        
        
        JSONConverter<Audio> converter = IoC.get(JSONConverterRegistry.class).getConverter(Audio.class);
        putAccessTokenAndExecute(m, converter, new Callback<RPC.Result<Audio>>() {

            @Override
            public void call(RPC.Result<Audio> result) {
                if ( result.isError() ) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error occured when audio loading", result.getError());
                    if ( callback instanceof Errorback ){
                        ((Errorback) callback).error(result.getError());
                    }
                } else {
                    callback.call(result.getResult());
                }
            }
        });
    }

    private <T> void putAccessTokenAndExecute(final VKMethod m, final JSONConverter<T> jsonConverter, final Callback<RPC.Result<T>> callback) {
        AccessToken accessToken = IoC.get(AccessTokenProvider.class).getAccessToken();
        m.setParam(VKMethod.VK_ACCESS_TOKEN, accessToken.getAccessToken());
        RPC.Result<T> result = IoC.get(RPC.class).execute(m, new JSONResponceConverter(jsonConverter));
        callback.call(result);
    }

}
