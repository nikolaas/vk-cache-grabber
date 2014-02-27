package org.ns.vkcachegrabber.vk.convert;

/**
 *
 * @author stupak
 */
public class ConvertException extends Exception {

    public ConvertException() {
        super();
    }
    
    public ConvertException(String message) {
        super(message);
    }
    
    public ConvertException(Throwable clause) {
        super(clause);
    }
    
    public ConvertException(String message, Throwable clause) {
        super(message, clause);
    }
        
}
