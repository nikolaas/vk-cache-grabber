package org.ns.vkcachegrabber.api;

import java.util.Map;

/**
 *
 * @author stupak
 */
public interface Openable {

    String getOpenableType();
    Map<String, Object> getParameters();
    Object getParameter(String key);
}
