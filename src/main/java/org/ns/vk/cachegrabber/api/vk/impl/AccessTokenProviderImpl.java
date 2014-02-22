package org.ns.vk.cachegrabber.api.vk.impl;

import com.googlecode.vkapi.HttpVkApi;
import com.googlecode.vkapi.VkApi;
import org.ns.vk.cachegrabber.api.vk.AccessToken;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.CookiePolicy;
import org.ns.func.Callback;
import org.ns.ioc.IoC;
import org.ns.vk.cachegrabber.api.Application;
import org.ns.vk.cachegrabber.api.vk.AccessTokenProvider;
import org.ns.vk.cachegrabber.api.vk.VKMethod;

/**
 *
 * @author stupak
 */
public class AccessTokenProviderImpl implements AccessTokenProvider {

    private final AccessTokenResponceHandler handler;
    private final HttpClient httpClient;
    private final VKMethod authorizeMethod;
    private final VKMethod loginMethod;
    private AccessToken accessToken;
    
    public AccessTokenProviderImpl() {
        this.httpClient = IoC.get(HttpClient.class);
        this.handler = new AccessTokenResponceHandler();
        this.authorizeMethod = VKMethod.authorizeMethodTemplate()
                .param(VKMethod.VK_CLIENT_ID, IoC.get(Application.class).getVkClientId())
                .param(VKMethod.VK_REDIRECT_URL, VKMethod.DEFAULT_REDIRECT_URL)
                .param(VKMethod.VK_DISPLAY, VKMethod.VK_DISPLAY_POPUP)
                .param(VKMethod.VK_SCOPE, VKMethod.VK_SCOPE_AUDIO)
                .param(VKMethod.VK_RESPONSE_TYPE, VKMethod.VK_RESPONSE_TYPE_TOKEN)
                .param(VKMethod.VK_API_VERSION, VKMethod.VK_API_VERSION_LAST)
                .build();
        this.loginMethod = VKMethod.loginMethodTemplate()
                .param("act", "login")
                .param("soft", "1")
                .param("q", "1")
                .param("from_host", "oauth.vk.com")
                .param("expire", "0")
                .build();
    }
    
    @Override
    public void getAccessToken(Callback<AccessToken> callback) {
        /*if ( accessToken != null ) {
            long current = System.currentTimeMillis() / 1000;
            if ( current - accessToken.getCreated() < accessToken.getExpiresIn() ) {
                requestNewAccessToken(callback);
            } else {
                callback.call(accessToken);
            }
        } else {
            requestNewAccessToken(callback);
        }*/
    }
    
    private void requestNewAccessToken(Callback<AccessToken> callback) {
        try {
            HttpUriRequest request = RpcUtils.toHttpRequest(authorizeMethod);
            HttpResponse response = httpClient.execute(request);
            Map<String, String> params = new HashMap<>();
            request.abort();
            String content = RpcUtils.read(response.getEntity().getContent());
            String ip_h = RpcUtils.findParamValue(content, "ip_h");
            String to = RpcUtils.findParamValue(content, "to");
        } catch (IOException ex) {
            Logger.getLogger(AccessTokenProviderImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static class AccessTokenResponceHandler implements RPC.ResponceHandler<AccessToken> {

        @Override
        public RPC.Result<AccessToken> handle(HttpResponse response, HttpUriRequest request) {
            int statusCode = response.getStatusLine().getStatusCode();
            RPC.Result<AccessToken> result;
            if ( statusCode != 200 ) {
                Exception ex = new Exception(response.getStatusLine().getReasonPhrase());
                result = new RPC.Result(ex);
            } else {
                // Теперь в след редиректе необходимый токен
                String headerLocation = response.getFirstHeader("location").getValue();
                // Просто спарсим его сплитами
                String access_token = headerLocation.split("#")[1].split("&")[0].split("=")[1];
                result = new RPC.Result(new AccessToken(access_token, 0, 0, null));
            }
            return result;
        }
        
    }
    
}
