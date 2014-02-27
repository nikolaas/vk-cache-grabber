package org.ns.vkcachegrabber.vk.json;

/**
 *
 * @author stupak
 */
public class JSONConvertException extends Exception {
    
    public JSONConvertException() {
        super();
    }
    
    public JSONConvertException(String message) {
        super(message);
    }
    
    public JSONConvertException(Throwable clause) {
        super(clause);
    }
    
    public JSONConvertException(String message, Throwable clause) {
        super(message, clause);
    }
    
}
