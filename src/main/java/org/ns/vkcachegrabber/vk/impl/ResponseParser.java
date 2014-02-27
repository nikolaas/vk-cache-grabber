package org.ns.vkcachegrabber.vk.impl;

import org.apache.http.HttpResponse;

/**
 *
 * @author stupak
 */
public interface ResponseParser {
    Object parseReponce(HttpResponse response) throws Exception;
}
