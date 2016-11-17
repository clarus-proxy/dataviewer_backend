package eu.clarussecure.dataviewer.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.clarussecure.dataviewer.model.SecurityPolicyEndpoint;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//@CrossOrigin(origins = "http://127.0.0.1", maxAge = 3600)
@Component
@Path("/data")
public class DataResource {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * getPSQLData
     * @param tableName
     * @return
     * @throws Exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/pgsql/{table}")
    public String getPSQLData(@PathParam("table") String tableName) throws Exception {

        /*TODO prevent SQL injection ---> check that tableName is in security policy attributes ? */
        String sql = String.format("SELECT * FROM %s", tableName);
        JSONArray json = new JSONArray();
        
        List<Map<String,Object>> results = jdbcTemplate.queryForList(sql);
        results.forEach((result) -> {
            JSONObject obj = new JSONObject();
            result.entrySet().forEach((entry) -> {
                obj.put(entry.getKey(), entry.getValue());
            });
            json.add(obj);
        });
        
        return json.toString();
    }

    /**
     * getWFSData
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
