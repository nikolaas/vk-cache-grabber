package org.ns.vkcachegrabber.vk;

/**
 *
 * @author stupak
 */
public interface CredentialProvider {
    
    boolean  hasCredential(String userId);
    Credential getCredential(String userId);
    void setCredential(String userId, Credential credential);
    
}
