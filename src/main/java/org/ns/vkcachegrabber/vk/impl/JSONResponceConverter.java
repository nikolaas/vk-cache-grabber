package org.ns.vkcachegrabber.vk.impl;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.ns.util.Assert;
import org.ns.util.Strings;
import org.ns.vkcachegrabber.vk.json.JSONConverter;

/**
 *
 * @author stupak
 * @param <T>
 */
public class JSONResponceConverter<T> implements RPC.ResponceHandler<T> {

    private final JSONConverter<T> converter;

    public JSONResponceConverter(JSONConverter<T> adapter) {
        Assert.isNotNull(adapter, "converter is null.");
        this.converter = adapter;
    }
    
    @Override
    public RPC.Result<T> handle(HttpResponse response, HttpUriRequest request) throws Exception {
        Throwable error = null;
        JSONObject json = null;
        try {
            json = parseReponce(response);
        } catch (IOException | ParseException ex) {
            error = ex;
        }
        if ( error == null ) {
            return new RPC.Result<>(converter.convert(json));
        } else {
            return new RPC.Result<>(error);
        }
    }
    
    private JSONObject parseReponce(HttpResponse response) throws IOException, ParseException {
        JSONObject jsonResponse = null;
        String content = RpcUtils.read(response.getEntity().getContent());
        if ( !Strings.empty(content) ) {
            JSONParser parser = new JSONParser();
            jsonResponse = (JSONObject) parser.parse(content);
        }
        return jsonResponse;
    }
    
}
