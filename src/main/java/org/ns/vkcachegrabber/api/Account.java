package org.ns.vkcachegrabber.api;

import java.io.Serializable;

/**
 *
 * @author stupak
 */
public interface Account extends Serializable {

    String getUserId();
    String getUserName();
    
}
