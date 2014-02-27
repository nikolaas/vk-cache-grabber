package org.ns.vkcachegrabber.vk.convert.json;

import org.ns.vkcachegrabber.vk.convert.ConvertException;

/**
 *
 * @author stupak
 */
public class JSONConvertException extends ConvertException {
    
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
