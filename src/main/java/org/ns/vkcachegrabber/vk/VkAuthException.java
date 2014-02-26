package org.ns.vkcachegrabber.vk;

/**
 *
 * @author stupak
 */
public class VkAuthException extends VkException {
    
    public VkAuthException() {
        super();
    }
    
    public VkAuthException(String message) {
        super(message);
    }
    
    public VkAuthException(Throwable clause) {
        super(clause);
    }
    
    public VkAuthException(String message, Throwable clause) {
        super(message, clause);
    }
    
}
