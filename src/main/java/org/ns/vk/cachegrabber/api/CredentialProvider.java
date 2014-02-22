package org.ns.vk.cachegrabber.api;

/**
 *
 * @author stupak
 */
public interface CredentialProvider {
    
    Credential getCredential(String userId);
}
