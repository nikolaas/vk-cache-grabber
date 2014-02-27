package org.ns.vkcachegrabber.vk.impl;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.ns.util.Strings;

/**
 *
 * @author stupak
 */
public class JSONResponseParser implements ResponseParser {

    /**
     * json от vk содержит в себе результирующий объект под ключом "response"
     */
    private static final String VK_RESPONSE = "response";

    @Override
    public JSONAware parseReponce(HttpResponse response) throws IOException, ParseException {
        JSONObject jsonResponse = null;
        String content = RpcUtils.read(response.getEntity().getContent());
        if ( !Strings.empty(content) ) {
            JSONParser parser = new JSONParser();
            jsonResponse = (JSONObject) parser.parse(content);
        }
        JSONAware json = null;
        if ( jsonResponse != null ) {
            json = (JSONAware) jsonResponse.get(VK_RESPONSE);
        }
        return json;
    }
    
}
