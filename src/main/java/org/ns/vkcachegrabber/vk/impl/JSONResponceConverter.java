package org.ns.vkcachegrabber.vk.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.ns.util.Assert;
import org.ns.util.Strings;
import org.ns.vkcachegrabber.vk.json.JSONConvertException;
import org.ns.vkcachegrabber.vk.json.JSONConverter;

/**
 *
 * @author stupak
 * @param <T>
 */
public class JSONResponceConverter<T> implements RPC.ResponceHandler<T> {

    /**
     * json от vk содержит в себе результирующий объект под ключом "response"
     */
    private static final String VK_RESPONSE = "response";
    
    private final JSONConverter<T> converter;

    public JSONResponceConverter(JSONConverter<T> adapter) {
        Assert.isNotNull(adapter, "converter is null.");
        this.converter = adapter;
    }
    
    @Override
    public RPC.Result<T> handle(HttpResponse response, HttpUriRequest request) {
        Throwable error = null;
        JSONObject jsonResponse = null;
        try {
            jsonResponse = parseReponce(response);
        } catch (IOException | ParseException ex) {
            error = ex;
        }
        T object = null;
        if ( jsonResponse != null ) {
            try {
                JSONAware json = (JSONAware) jsonResponse.get(VK_RESPONSE);
                if ( json instanceof JSONObject ) {
                    object = convertToObject((JSONObject) json);
                } else if ( json instanceof JSONArray ) {
                    List<T> list = convertToList((JSONArray) json);
                    object = list.get(0);
                }
            } catch (JSONConvertException ex) {
                error = ex;
            }
        }
        if ( error == null ) {
            return new RPC.Result<>(object);
        } else {
            return new RPC.Result<>(error);
        }
    }
    
    private T convertToObject(JSONObject jsonObject) throws JSONConvertException {
        return converter.convert(jsonObject);
    }
    
    private List<T> convertToList(JSONArray jsonArray) throws JSONConvertException {
        List<T> list = new ArrayList<>();
        for ( Object json : jsonArray ) {
            if ( json instanceof JSONObject ) {
                T object = convertToObject((JSONObject) json);
                list.add(object);
            }
        }
        return list;
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
