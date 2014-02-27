package org.ns.vkcachegrabber.vk.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.ns.func.Function;
import org.ns.ioc.IoC;
import org.ns.vkcachegrabber.vk.VKMethod;

/**
 *
 * @author stupak
 */
public class RPC {
    
    private final Map<String, ResponseParser> parsers = new HashMap<>();

    public RPC register(String responseType, ResponseParser parser) {
        parsers.put(responseType, parser);
        return this;
    }
    
    public <T> Result<T> execute(VKMethod method, Function<ParsedObject, T> function) {
        Result<T> result;
        HttpUriRequest request;
        HttpResponse response;
        try {
            request = RpcUtils.toHttpRequest(method);
            response = IoC.get(HttpClient.class).execute(request);
        } catch (IOException ex) {
            return new Result<>(ex);
        }
        String responseType = getResponseType(response);
        ResponseParser parser = parsers.get(responseType);
        if ( parser == null ) {
            result = new Result<>(new NullPointerException("Response type \"" + responseType + "\" unsupported."));
        } else {
            Throwable error = null;
            T obj = null;
            try {
                Object parsedResponse = parser.parseReponce(response);
                obj = function.apply(new ParsedObject(responseType, parsedResponse));
            } catch (Exception ex) {
                error = ex;
            }
            if ( error == null ) {
                result = new Result<>(obj);
            } else {
                result = new Result<>(error);
            }
        }
        return result;
    }
    
    private String getResponseType(HttpResponse response) {
        String contentType = response.getEntity().getContentType().getValue();
        return contentType.split(";")[0];
    }
    
    public static class Result<T> {
        private final T result;
        private final Throwable error;

        public Result(T result) {
            this(result, null);
        }
        
        public Result(Throwable error) {
            this(null, error);
        }
        
        public Result(T result, Throwable error) {
            this.result = result;
            this.error = error;
        }

        public T getResult() {
            return result;
        }

        public Throwable getError() {
            return error;
        }

        public boolean isError() {
            return error != null;
        }
    }
}
