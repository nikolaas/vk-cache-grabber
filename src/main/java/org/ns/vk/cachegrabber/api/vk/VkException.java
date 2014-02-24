package org.ns.vk.cachegrabber.api.vk;

/**
 *
 * @author stupak
 */
public class VkException extends Exception {

    public VkException() {
        super();
    }
    
    public VkException(String message) {
        super(message);
    }
    
    public VkException(Throwable clause) {
        super(clause);
    }
    
    public VkException(String message, Throwable clause) {
        super(message, clause);
    }
    
}
