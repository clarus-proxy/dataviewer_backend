package eu.clarussecure.dataviewer.resources;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.clarussecure.dataviewer.model.SecurityPolicyEndpoint;

@Component
@Path("/data")
public class CountDataResource {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * getPSQLData
     * 
     * @param tableName
     * @return
     * @throws Exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("count/pgsql/{table}")
    public String getCountPagePGSQL(@PathParam("table") String tableName)
            throws Exception {

        /*
         * TODO prevent SQL injection ---> check that tableName is in security
         * policy attributes ?
         */

        String sql = String.format("SELECT count(*) FROM %s ", tableName);
        JSONArray json = new JSONArray();

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        json.add(results.get(0));

        return json.toString();
    }

    /**
     * getWFSData
     * 
     * @param layerName
     * @return
     * @throws Exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("count/wfs/{layer}")
    public String getCountPageWFS(@PathParam("layer") String layerName)
            throws Exception {
        // String
        RestTemplate restTemplate = new RestTemplate();
        Gson gson = new Gson();

        String json = new EndpointListResource().getProtocolEndpoints("wfs");

        Type endpointListType = new TypeToken<ArrayList<SecurityPolicyEndpoint>>() {
        }.getType();
        List<SecurityPolicyEndpoint> wfsEndpoints = gson.fromJson(json,
                endpointListType);
        String wfsEndpointUrl = wfsEndpoints.stream().findFirst().orElse(null)
                .getBaseUrl();
        String httpUrl = String
                .format("%s?request=GetFeature&version=1.1.0&typeName=%s&startIndex=0&maxFeatures=1&outputFormat=application/json",
                        wfsEndpointUrl, layerName);
        String result = restTemplate.getForObject(httpUrl, String.class);

        return getTotalFeatures(result);
    }

    // Fonction qui retourne le nombre de Features total
    public static String getTotalFeatures(String result) {

        String resultSplitted[] = result.split(",");
        String resultSplitted1[] = resultSplitted[1].split(":");
        String total = resultSplitted1[1];
        return total;

    }
}
