package org.ns.vk.cachegrabber.api.vk.impl;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.ns.ioc.IoC;
import org.ns.vk.cachegrabber.api.vk.VKMethod;

/**
 *
 * @author stupak
 */
public class RPC {
    
    private final HttpClient httpClient;

    public RPC() {
        this.httpClient = IoC.get(HttpClient.class);
    }
    
    public <T> Result<T> execute(VKMethod method, ResponceHandler<T> handler) {
        HttpUriRequest request = null;
        HttpResponse response = null;
        Throwable error = null;
        try {
            request = RpcUtils.toHttpRequest(method);
            response = httpClient.execute(request);
        } catch (IOException ex) {
            error = ex;
        }
        Result<T> result;
        if ( error == null ) {
            try {
                result = handler.handle(response, request);
            } catch (Exception ex) {
                result = new Result<>(ex);
            }
        } else {
            result = new Result<>(error);
        }
        return result;
    }
    
    public static interface ResponceHandler<T> {
        Result<T> handle(HttpResponse response, HttpUriRequest request) throws Exception;
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
