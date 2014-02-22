package org.ns.vk.cachegrabber.api.vk.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.ns.util.Assert;
import org.ns.vk.cachegrabber.json.JSONConverter;

/**
 *
 * @author stupak
 */
public class JSONResponceConverter<T> implements RPC.ResponceHandler<T> {

    private final JSONConverter<T> converter;

    public JSONResponceConverter(JSONConverter<T> adapter) {
        Assert.isNotNull(adapter, "converter is null.");
        this.converter = adapter;
    }
    
    @Override
    public RPC.Result<T> handle(HttpResponse response, HttpUriRequest request) {
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
    
    private JSONObject parseReponce(HttpResponse response) 
            throws UnsupportedEncodingException, IOException, ParseException {
        JSONObject jsonResponse = null;
        Reader r = null;
        String encoding = null;
        try {
            InputStream responceStream = null;
            if ( response != null ) {
                responceStream = response.getEntity().getContent();
                encoding = response.getEntity().getContentEncoding().getValue();
            }
            if ( responceStream != null ) {
                JSONParser parser = new JSONParser();
                r = new InputStreamReader(responceStream, encoding);
                jsonResponse = (JSONObject) parser.parse(r);
            }
        } finally {
            org.ns.util.Utils.closeSilent(r);
        }
        return jsonResponse;
    }
    
}
