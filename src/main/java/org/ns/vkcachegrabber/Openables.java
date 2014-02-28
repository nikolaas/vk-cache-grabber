package org.ns.vkcachegrabber;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.ns.util.Strings;
import org.ns.vkcachegrabber.api.Openable;
import org.ns.vkcachegrabber.doc.AuthHandler;
import org.ns.vkcachegrabber.doc.ErrorHandler;
import org.ns.vkcachegrabber.vk.Credential;

/**
 *
 * @author stupak
 */
public class Openables {

    public static final String SCHEME_DEVIDER = "://";
    public static final String PATCH_DEVIDER = "/";
    public static final String FIRST_PARAM = "?";
    public static final String PARAM = "&";
    public static final String PARAM_VALUE_DEVIDER = "=";
    
    private Openables() {
    }
    
    public static OpenableBuilder builder() {
        return new OpenableBuilder();
    }
    
    public static Openable buildAuthOpenable() {
        return buildAuthOpenable(null);
    }
    
    public static Openable buildAuthOpenable(Credential old) {
        OpenableBuilder builder = builder()
                .openableType(AuthHandler.OPENABLE_TYPE);
        if ( old != null ) {
            builder.addParam(AuthHandler.LOGIN, old.getEmail());
            builder.addParam(AuthHandler.PASSWORD, old.getPassword());
            builder.addParam(AuthHandler.INVALID, true);
        }
        return builder.build();
    }
    
    public static Openable buildErrorOpenable() {
        OpenableBuilder builder = builder()
                .openableType(ErrorHandler.OPENABLE_TYPE);
        return builder.build();
    }
    
    public static Map<String, String> getParams(URI uri) {
        return getParams(uri.toString());
    }
    
    public static Map<String, String> getParams(String uri) {
        Map<String, String> params = new HashMap<>();
        int paramStringStart = uri.indexOf("?");
        if ( paramStringStart < 0 ) {//в некоторых uri параметры лежат не за '?', а за '#'
            paramStringStart = uri.indexOf("#");
            if ( paramStringStart < 0 ) {
                return params;
            }
        }
        String query = uri.substring(paramStringStart + 1);
        if ( Strings.empty(query) ) {
            return params;
        }
        for ( String param : query.split("&") ) {
            String[] parsedParam = param.split("=");
            String name = parsedParam[0];
            String value = null;
            if ( parsedParam.length == 2 ) {
                value = parsedParam[1];
            }
            params.put(name, value);
        }
        return params;
    }
}
