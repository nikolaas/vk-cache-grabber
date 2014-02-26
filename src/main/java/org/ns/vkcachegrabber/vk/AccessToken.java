package org.ns.vkcachegrabber.vk;

import java.io.Serializable;
import java.util.Map;
import org.ns.util.Strings;

/**
 *
 * @author stupak
 */
public class AccessToken implements Serializable {

    private static final String PARAM_ACCESS_TOKEN = "access_token";
    private static final String PARAM_EXPIRES_IN = "expires_in";
    private static final String PARAM_USER_ID = "user_id";
    
    private final String accessToken;
    
    /**
     * время жизни (в секундах)
     */
    private final long expiresIn;
    
    private final String userId;

    public AccessToken(String accessToken, long expiresIn, String userId) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "AccessToken{" + "accessToken=" + accessToken + ", expiresIn=" + expiresIn + ", userId=" + userId + '}';
    }
    
    public static AccessToken from(Map<String, String> params) {
        String token = params.get(PARAM_ACCESS_TOKEN);
        String userId = params.get(PARAM_USER_ID);
        String sExpiresIn = params.get(PARAM_EXPIRES_IN);
        long expiresIn = 0;
        if ( !Strings.empty(sExpiresIn) ) {
            expiresIn = Long.parseLong(sExpiresIn);
        }
        return new AccessToken(token, expiresIn, userId);
    }
}
