package org.ns.vkcachegrabber;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import org.ns.vkcachegrabber.api.Openable;

/**
 * immutable
 * @author stupak
 */
class OpenableImpl implements Openable {
    
    private final String openableType;
    private final Map<String, Object> params;
    
    OpenableImpl(OpenableBuilder builder) {
        this.openableType = builder.getOpenableType();
        this.params = builder.getParams();
    }
    
    @Override
    public String getOpenableType() {
        return openableType;
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(params);
    }

    @Override
    public Object getParameter(String key) {
        return getParam(key, null);
    }

    @Override
    public Object getParameter(String key, Object defaultValue) {
        return getParam(key, defaultValue);
    }
    
    private Object getParam(String key, Object defaultValue) {
        Object value = params.get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.openableType);
        hash = 67 * hash + Objects.hashCode(this.params);
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
        final OpenableImpl other = (OpenableImpl) obj;
        if (!Objects.equals(this.openableType, other.openableType)) {
            return false;
        }
        if (!Objects.equals(this.params, other.params)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OpenableImpl{" + "openableType=" + openableType + ", params=" + params + '}';
    }
    
}
