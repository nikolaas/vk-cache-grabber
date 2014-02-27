package org.ns.vkcachegrabber.vk.impl;

import java.util.Objects;

/**
 *
 * @author stupak
 */
public class ParsedObject {

    private final String type;
    private final Object object;

    public ParsedObject(String type, Object object) {
        this.type = type;
        this.object = object;
    }

    public String getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.type);
        hash = 23 * hash + Objects.hashCode(this.object);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParsedObject other = (ParsedObject) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.object, other.object)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ParsedObject{" + "type=" + type + ", object=" + object + '}';
    }
    
}
