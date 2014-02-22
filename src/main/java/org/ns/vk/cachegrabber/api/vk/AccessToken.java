package org.ns.vk.cachegrabber.api.vk;

/**
 *
 * @author stupak
 */
public class AccessToken {

    private final String accessToken;
    
    /**
     * время жизни (в секундах)
     */
    private final long expiresIn;
    
    /**
     * время получения токена
     */
    private final long created;
    
    private final String userId;

    public AccessToken(String accessToken, long expiresIn, long created, String userId) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.created = created;
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public long getCreated() {
        return created;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "AccessToken{" + "accessToken=" + accessToken + ", expiresIn=" + expiresIn + ", userId=" + userId + '}';
    }
    
}
