package org.ns.vkcachegrabber.vk;

/**
 *
 * @author stupak
 */
public class Credential {

    private final String email;
    private final char[] password;

    public Credential(String email, char[] password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public char[] getPassword() {
        return password;
    }
    
    public static String toString(char[] password) {
        return new String(password);
    }
}
