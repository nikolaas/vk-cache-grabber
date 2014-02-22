package org.ns.vk.cachegrabber.api.vk.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.ns.vk.cachegrabber.api.vk.VKMethod;

/**
 *
 * @author stupak
 */
public class RpcUtils {

    private RpcUtils() {
    }

    private static final Map<String, HttpUriRequestFactory> factories;
    static {
        factories = new HashMap<>();
        factories.put(VKMethod.METHOD_POST, new HttpUriPostRequestFactory());
        factories.put(VKMethod.METHOD_GET, new HttpUriGetRequestFactory());
    }
    
    public static HttpUriRequest toHttpRequest(VKMethod method) {
        HttpUriRequestFactory factory = factories.get(method.getHttpmMethod());
        if ( factory == null ) {
            throw new NullPointerException("Unsupported http method");
        }
        return factory.create(method);
    }
    
    public static interface HttpUriRequestFactory {
        HttpUriRequest create(VKMethod method);
    }
    
    private static class HttpUriPostRequestFactory implements HttpUriRequestFactory{
        
        @Override
        public HttpUriRequest create(VKMethod method) {
            return new HttpPost(method.getUri());
        }
    }
    
    private static class HttpUriGetRequestFactory implements HttpUriRequestFactory{
        
        @Override
        public HttpUriRequest create(VKMethod method) {
            return new HttpGet(method.getUri());
        }
    }
    
    public static String read(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ( (line = reader.readLine()) != null ) {
                sb.append(line).append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(RpcUtils.class.getName()).log(Level.SEVERE, null, ex);
            sb = null;
        }
        return sb == null ? null : sb.toString();
    }
    
    private static final String paramNameTemplate = "name=\"{0}\"";
    
    public static String findParamValue(String content, String paramName) {
        String param = MessageFormat.format(paramNameTemplate, paramName);
        int paramIndex = content.indexOf(param);
        if ( paramIndex < 0 ) {
            return null;
        }
        int valueIndex = content.indexOf("value", paramIndex);
        if ( valueIndex < 0 ) {
            return null;
        }
        int valueStart = valueIndex + "value".length() + 2;//+1 на = и +1 на "
        int valueEnd = content.indexOf("\"", valueStart);
        String value = content.substring(valueStart, valueEnd);
        return value;
    }
}
