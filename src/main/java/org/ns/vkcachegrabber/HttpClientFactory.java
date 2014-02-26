package org.ns.vkcachegrabber;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.ns.ioc.Factory;

/**
 *
 * @author stupak
 */
public class HttpClientFactory implements Factory<HttpClient> {

    private HttpClient httpClient;
    
    
    @Override
    public HttpClient createInstance() throws Exception {
        if ( httpClient == null ) {
            this.httpClient = HttpClientBuilder.create().build();
        }
        return httpClient;
    }
    
}
