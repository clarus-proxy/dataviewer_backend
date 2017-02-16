package eu.clarussecure.dataviewer.plugins.wfs;


import java.net.InetSocketAddress;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import eu.clarussecure.dataviewer.plugins.util.UrlBuilder;
import eu.clarussecure.dataviewer.spi.ProtocolResource;

public class WfsResource implements ProtocolResource {

    @Override
    public String getProtocolName() {
        return "WFS";
    }

    @Override
    public String getClearData(String protocol, InetSocketAddress endpoint, String store, String collection) {
    	RestTemplate restTemplate = new RestTemplate();
        //parse URL from endpoint's data
    	String wfsEndpointUrl = UrlBuilder.buildHttpUrl(endpoint);
        String httpUrl = String
                .format("%s/%s/%s?request=GetFeature&version=1.1.0&typeName=%s&outputFormat=application/json",
                        wfsEndpointUrl, store, protocol, collection);
        // add request header
        HttpHeaders headers = new HttpHeaders();
        headers.set("CLARUS", "unprotected");
        HttpEntity entity = new HttpEntity(headers);
        HttpEntity<String> response = restTemplate.exchange(httpUrl,
                HttpMethod.GET, entity, String.class);
        return (response.getBody());
    }

    @Override
	public String getProtectedData(String protocol, InetSocketAddress endpoint, String store, String collection) {
    	RestTemplate restTemplate = new RestTemplate();
        Gson gson = new Gson();
        TreeMap<Integer, String> cspList = new TreeMap<Integer, String>();
    
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> response = null;
        String result = null;
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = new  JSONObject();
        
        String wfsEndpointUrl = UrlBuilder.buildHttpUrl(endpoint);

        // request meta data
        String httpHead = String.format(
                "%s?request=HEAD&typeName=%s&outputFormat=application/json",
                wfsEndpointUrl, collection);
        String headResponse = restTemplate.getForObject(httpHead, String.class);

        cspList.put(1, "csp1");
        cspList.put(2, "csp2");
        cspList.put(3, "csp3");
        for (Map.Entry<Integer, String> entryCSP : cspList.entrySet()) {

            // add request header
            headers.set("CLARUS_protected", String.valueOf(entryCSP.getKey()));
            String httpUrl = String
                    .format("%s?request=GetFeature&version=1.1.0&typeName=%s&outputFormat=application/json",
                            wfsEndpointUrl, collection);
            HttpEntity entity = new HttpEntity(headers);
            response = restTemplate.exchange(httpUrl, HttpMethod.GET, entity,
                    String.class);
            JSONObject jsonResul = null;
			try {
				jsonResul = (JSONObject) parser.parse(response.getBody());
			} catch (ParseException e) {
				e.printStackTrace();
			}
            jsonResult.put(entryCSP.getKey(), jsonResul);
        }
        return (jsonResult.toJSONString());
	}

}
