package org.ns.vkcachegrabber.vk;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.ns.util.Strings;

/**
 *
 * @author stupak
 */
public class VKMethod {
    
    // vk app name "vk-cache-reader"
    
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    
    public static final String VK_SCHEME = "https";
    
    public static final String VK_HOST_API = "api.vk.com";
    public static final String VK_HOST_AUTHORIZE = "oauth.vk.com";
    public static final String VK_HOST_LOGIN = "login.vk.com";
    
    /**
     * patches
     */
    public static final class M {
        
        public static final String VK_METHOD_API = "/method";
        public static final String VK_METHOD_AUTHORIZE = "/authorize";
        
        public static final class Audio {
        
            private static final String VK_AUDIO = VK_METHOD_API + "/audio";
            public static final String GET = VK_AUDIO + ".get";
            public static final String GET_BY_ID = VK_AUDIO + ".getById";
            
        }
        
        public static final class Users {
        
            private static final String VK_USER = VK_METHOD_API + "/users";
            public static final String GET = VK_USER + ".get";
            
        }
    }
    
    public static final String USER_IDS = "user_ids";
    /**
     * vk httpMethods
     */
    
    /**
     * params
     */
    public static final String VK_OWNER_ID = "oid";
    public static final String VK_ACCESS_TOKEN = "access_token";
    public static final String VK_AUDIOS = "audios";
    
    public static final String VK_CLIENT_ID = "client_id";
    public static final String VK_REDIRECT_URL = "redirect_uri";
    public static final String VK_DISPLAY = "display";
    public static final String VK_SCOPE = "scope";
    public static final String VK_RESPONSE_TYPE = "response_type";
    public static final String VK_API_VERSION = "v";
    
    public static final String DEFAULT_REDIRECT_HOST = "oauth.vk.com";
    public static final String DEFAULT_REDIRECT_URL = "https://oauth.vk.com/blank.html";
    
    public static final String VK_DISPLAY_PAGE = "page";
    public static final String VK_DISPLAY_POPUP = "popup";
    public static final String VK_RESPONSE_TYPE_CODE = "code";
    public static final String VK_RESPONSE_TYPE_TOKEN = "token";
    public static final String VK_SCOPE_AUDIO = "audio";
    public static final String VK_API_VERSION_LAST = "5.10";
    
    public static class Builder {
    
        private String httpMethod;
        private String scheme;
        private String host;
        private String vkMethod;
        private final Map<String, String> params = new HashMap<>();

        private Builder httpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        private Builder scheme(String scheme) {
            this.scheme = scheme;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder vkMethod(String vkMethod) {
            this.vkMethod = vkMethod;
            return this;
        }

        public Builder param(String key, String value) {
            params.put(key, value);
            return this;
        }
        
        public VKMethod build() {
            return new VKMethod(this);
        }
        
    }
    
    public static Builder post() {
        return new Builder().httpMethod(METHOD_POST).scheme(VK_SCHEME);
    }
    
    public static Builder get() {
        return new Builder().httpMethod(METHOD_GET).scheme(VK_SCHEME);
    }
    
    public static Builder apiMethodTemplate() {
        return post().host(VK_HOST_API);
    }
    
    public static Builder authorizeMethodTemplate() {
        return post().host(VK_HOST_AUTHORIZE).vkMethod(M.VK_METHOD_AUTHORIZE);
    }
    
    public static Builder loginMethodTemplate() {
        return post().host(VK_HOST_LOGIN);
    }

    private String httpmMethod;
    private final URIBuilder uriBuilder;

    private VKMethod(Builder builder) {
        httpmMethod = builder.httpMethod;
        uriBuilder = new URIBuilder()
            .setScheme(builder.scheme)
            .setHost(builder.host);
        if ( !Strings.empty(builder.vkMethod) ) {
            uriBuilder.setPath(builder.vkMethod);
        }
        if ( !builder.params.isEmpty() ) {
            for ( Map.Entry<String, String> param : builder.params.entrySet() ) {
                uriBuilder.setParameter(param.getKey(), param.getValue());
            }
        }
    }

    public void setHttpmMethod(String httpmMethod) {
        this.httpmMethod = httpmMethod;
    }

    public String getHttpmMethod() {
        return httpmMethod;
    }
    
    public URI getUri() {
        URI uri = null;
        try {
            uri = uriBuilder.build();
        } catch (URISyntaxException ex) {
            Logger.getLogger(VKMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uri;
    }
    public String getScheme() {
        return uriBuilder.getScheme();
    }
    
    public String getHost() {
        return uriBuilder.getHost();
    }
    
    public String getMethod() {
        return uriBuilder.getPath();
    }
    
    public void setParam(String key, String value) {
        uriBuilder.setParameter(key, value);
    }
    
    public String getParam(String key) {
        for (NameValuePair param : uriBuilder.getQueryParams() ) {
            if ( param.getName().equals(key) ) {
                return param.getValue();
            }
        }
        return null;
    }
}
