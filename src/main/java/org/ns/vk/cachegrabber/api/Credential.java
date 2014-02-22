package org.ns.vk.cachegrabber.api;

/**
 *
 * @author stupak
 */
public class Credential {

    private final String email;
    private final byte[] password;

    public Credential(String email, byte[] password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getPassword() {
        return password;
    }
    
    public static String toString(byte[] password) {
        return new String(password);
    }
}
