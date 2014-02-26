package org.ns.vkcachegrabber.vk.impl;

import org.ns.vkcachegrabber.vk.Credential;
import org.ns.vkcachegrabber.vk.CredentialProvider;

/**
 *
 * @author stupak
 */
public class CredentialProviderImpl implements CredentialProvider {

    @Override
    public boolean hasCredential(String userId) {
        return false;
    }

    @Override
    public Credential getCredential(String userId) {
        return null;
    }

    @Override
    public void setCredential(String userId, Credential credential) {
        
    }
    
}
