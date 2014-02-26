package org.ns.vkcachegrabber;

import java.util.HashMap;
import java.util.Map;
import org.ns.util.Assert;
import org.ns.util.Strings;
import org.ns.vkcachegrabber.api.Openable;

/**
 *
 * @author stupak
 */
public class OpenableBuilder {
    
    private String openableType;
    private Map<String, Object> params = new HashMap<>();

    public String getOpenableType() {
        return openableType;
    }

    public void setOpenableType(String openableType) {
        Assert.isTrue(!Strings.empty(openableType), "openableType is empty");
        this.openableType = openableType;
    }

    public OpenableBuilder openableType(String openableType) {
        setOpenableType(openableType);
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        Assert.isNotNull(params, "params is null");
        this.params = params;
    }

    public OpenableBuilder params(Map<String, Object> params) {
        setParams(params);
        return this;
    }

    public OpenableBuilder addParam(String key, Object value) {
        getParams().put(key, value);
        return this;
    }

    public Openable build() {
        return new OpenableImpl(this);
    }
}
