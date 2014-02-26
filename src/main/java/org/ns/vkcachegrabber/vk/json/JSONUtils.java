package org.ns.vkcachegrabber.vk.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author stupak
 */
public class JSONUtils {

    private static final String JSON_NAME_DEVIDER = "_";
    
    private JSONUtils() {
    }

    public static String toJsonNotation(String propertyName) {
        List<String> parts = new ArrayList<>();
        StringBuilder part = new StringBuilder();
        for ( int i = 0; i < propertyName.length(); i++ ) {
            char ch = propertyName.charAt(i);
            if ( Character.isLowerCase(ch) ) {
                part.append(ch);
            } else if ( Character.isUpperCase(ch) ) {
                parts.add(part.toString());
                part.setLength(0);
                part.append(Character.toLowerCase(ch));
            }
        }
        parts.add(part.toString());
        StringBuilder jsonName = new StringBuilder();
        Iterator<String> i = parts.iterator();
        while( i.hasNext() ) {
            jsonName.append(i.next());
            if ( i.hasNext() ) {
                jsonName.append(JSON_NAME_DEVIDER);
            }
        }
        return jsonName.toString();
    }
    
    public static String toPojoNotation(String jsonName) {
        String[] parts = jsonName.split(JSON_NAME_DEVIDER);
        StringBuilder pojoName = new StringBuilder();
        boolean isFirst = true;
        for ( String part : parts) {
            String pojoPart;
            if ( isFirst ) {
                pojoPart = part;
                isFirst = false;
            } else {
                pojoPart = String.valueOf(Character.toUpperCase(part.charAt(0)));
                if ( part.length() > 1) {
                    pojoPart += part.substring(1);
                }
            }
            pojoName.append(pojoPart);
        }
        return pojoName.toString();
    }
}
