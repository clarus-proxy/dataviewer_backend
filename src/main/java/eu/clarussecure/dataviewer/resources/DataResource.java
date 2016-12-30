package eu.clarussecure.dataviewer.resources;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.clarussecure.dataviewer.model.SecurityPolicyEndpoint;

//@CrossOrigin(origins = "http://127.0.0.1", maxAge = 3600)
@Component
@Path("/data")
public class DataResource {

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
    @Path("/pgsql/unprotected/{table}")
    public String getPSQLData(@PathParam("table") String tableName,
            @QueryParam("limit") @DefaultValue("all") String limit,
            @QueryParam("start") @DefaultValue("0") long start)
            throws Exception {

        /*
         * TODO prevent SQL injection ---> check that tableName is in security
         * policy attributes ?
         */
        if (limit.equals(""))
            limit = "all";
        String sql = String.format("SELECT * FROM %s limit %s offset %d",
                tableName, limit, start);
        JSONArray json = new JSONArray();
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
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
     * getPSQLData
     * 
     * @param tableName
     * @return
     * @throws Exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/pgsql/protected/{table}")
    public String getPSQLProtectedData(@PathParam("table") String tableName,
            @QueryParam("limit") @DefaultValue("all") String limit,
            @QueryParam("start") @DefaultValue("0") long start)
            throws Exception {

        /*
         * TODO prevent SQL injection ---> check that tableName is in security
         * policy attributes ?
         */
        if (limit.equals(""))
            limit = "all";
        /*
         * meta data this function returns columns names protected and
         * unprotected
         */
        String head = String
                .format("SELECT column_name, concat(table_name,'.',column_name) as protected_column_name FROM information_schema.columns WHERE table_name = '%s' ",
                        tableName);
        List<Map<String, Object>> resultMetaData = jdbcTemplate
                .queryForList(head);
        JSONArray json = new JSONArray();
        TreeMap<Integer, String> cspList = new TreeMap<Integer, String>();
        resultMetaData.forEach((result) -> {
            JSONObject obj = new JSONObject();
            result.entrySet().forEach((entry) -> {
                //
                    Random rand = new Random();
                    int cspKey = rand.nextInt(3) + 1;
                    String value = (String) entry.getValue();
                    if (entry.getKey().equals("protected_column_name")) {
                        value = "csp" + cspKey + "." + entry.getValue();
                        String cspValue = value.substring(0, 4); // pour que la
                                                                 // fonction
                                                                 // reste
                                                                 // generique
                        cspList.put(cspKey, cspValue);
                    }
                    obj.put(entry.getKey(), value);
                });
        });

        /*
         * data for each CSP : CLARUS_PROTECTED(id csp, * )
         */
        for (Map.Entry<Integer, String> entryCSP : cspList.entrySet()) {
            String sql = String
                    .format("SELECT  CLARUS_PROTECTED(%d,*)  FROM %s limit %s offset %d",
                            entryCSP.getKey(), tableName, limit, start);
            sql = String.format("SELECT  *  FROM %s limit %s offset %d",
                    tableName, limit, start);
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            results.forEach((result) -> {
                JSONObject obj = new JSONObject();
                result.entrySet().forEach((entry) -> {
                    obj.put(entry.getKey(), entry.getValue());
                });
                json.add(obj);
            });
            json.add("end of csp");
        }

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
    @Path("wfs/unprotected/{layer}")
    public String getWFSData(@PathParam("layer") String layerName,
            @QueryParam("limit") @DefaultValue("1000000") long limit,
            @QueryParam("start") @DefaultValue("0") long start)
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
                .format("%s?request=GetFeature&version=1.1.0&typeName=%s&maxFeatures=%d&startIndex=%d&outputFormat=application/json",
                        wfsEndpointUrl, layerName, limit, start);

        // add request header
        HttpHeaders headers = new HttpHeaders();
        headers.set("CLARUS", "unprotected");
        HttpEntity entity = new HttpEntity(headers);
        HttpEntity<String> response = restTemplate.exchange(httpUrl,
                HttpMethod.GET, entity, String.class);
        return (response.getBody());
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
    @Path("wfs/protected/{layer}")
    public String getWFSProtectedData(@PathParam("layer") String layerName,
            @QueryParam("limit") @DefaultValue("1000000") long limit,
            @QueryParam("start") @DefaultValue("0") long start)
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
                .format("%s?request=GetFeature&version=1.1.0&typeName=%s&maxFeatures=%d&startIndex=%d&outputFormat=application/json",
                        wfsEndpointUrl, layerName, limit, start);

        // add request header
        HttpHeaders headers = new HttpHeaders();
        headers.set("CLARUS", "protected");
        HttpEntity entity = new HttpEntity(headers);
        HttpEntity<String> response = restTemplate.exchange(httpUrl,
                HttpMethod.GET, entity, String.class);

        return (response.getBody());
    }
}
