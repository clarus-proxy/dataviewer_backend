/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.clarussecure.dataviewer.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.clarussecure.dataviewer.model.SecurityPolicyEndpoint;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//@CrossOrigin(origins = "http://127.0.0.1", maxAge = 3600)
@Component
@Path("/data")
public class DataResource {

    /**
     * getWFSData
     *
     * @param layerName
     * @return
     * @throws Exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("wfs/{layer}")
    public String getWFSData(@PathParam("layer") String layerName) throws Exception {
        //String
        RestTemplate restTemplate = new RestTemplate();
        Gson gson = new Gson();

        String json = new EndpointListResource().getProtocolEndpoints("wfs");

        Type endpointListType = new TypeToken<ArrayList<SecurityPolicyEndpoint>>() {
        }.getType();
        List<SecurityPolicyEndpoint> wfsEndpoints = gson.fromJson(json, endpointListType);

        String wfsEndpointUrl = wfsEndpoints.stream().findFirst().orElse(null).getBaseUrl();

        String httpUrl = String.format("%s?request=GetFeature&version=1.1.0&typeName=%s&outputFormat=application/json", wfsEndpointUrl, layerName);

        return (restTemplate.getForObject(httpUrl, String.class));
    }
}
