package eu.clarussecure.dataviewer.resources;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    @Path("/pgsql/{table}")
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
         * this function returns columns names protected and unprotected
         */

        /*
         * serve as an example
         */
        String head = String
                .format("SELECT column_name, concat(table_name,'.',column_name) as protected_column_name FROM information_schema.columns WHERE table_name = '%s' ",
                        tableName);

        /*
         * real case
         */
        // String head = String
        // .format("SELECT CLARUS_METADATA(*)  WHERE table_name = '%s' ",
        // tableName);

        List<Map<String, Object>> resultMetaData = jdbcTemplate
                .queryForList(head);
        JSONArray json = new JSONArray();
        TreeMap<Integer, String> cspList = new TreeMap<Integer, String>();
        resultMetaData.forEach((result) -> {
            result.entrySet().forEach((entry) -> {

                /*
                 * serve as an example
                 */
                Random rand = new Random();
                int cspKey = rand.nextInt(3) + 1;
                String value = (String) entry.getValue();
                if (entry.getKey().equals("protected_column_name")) {
                    value = "csp" + cspKey + "." + entry.getValue();
                    String cspValue = value.substring(0, 4);
                    cspList.put(cspKey, cspValue);
                }

            });
        });

        /*
         * data for each CSP : CLARUS_PROTECTED(id csp, * )
         */
        for (Map.Entry<Integer, String> entryCSP : cspList.entrySet()) {

            /*
             * real case
             */
            String sql = String
                    .format("SELECT  CLARUS_PROTECTED(%d,*)  FROM %s limit %s offset %d",
                            entryCSP.getKey(), tableName, limit, start);
            /*
             * serve as an example
             */
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
    @Path("wfs/{layer}")
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
        TreeMap<Integer, String> cspList = new TreeMap<Integer, String>();
        String json = new EndpointListResource().getProtocolEndpoints("wfs");
       
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> response = null;
        String result = null;
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = new  JSONObject();
        
        Type endpointListType = new TypeToken<ArrayList<SecurityPolicyEndpoint>>() {
        }.getType();
        List<SecurityPolicyEndpoint> wfsEndpoints = gson.fromJson(json,
                endpointListType);

        String wfsEndpointUrl = wfsEndpoints.stream().findFirst().orElse(null)
                .getBaseUrl();

        // request meta data
        String httpHead = String.format(
                "%s?request=HEAD&typeName=%s&outputFormat=application/json",
                wfsEndpointUrl, layerName);
        String headResponse = restTemplate.getForObject(httpHead, String.class);

        /*
         * serve as an example
         */
        cspList.put(1, "csp1");
        cspList.put(2, "csp2");
        cspList.put(3, "csp3");
        for (Map.Entry<Integer, String> entryCSP : cspList.entrySet()) {

            // add request header
            headers.set("CLARUS_protected", String.valueOf(entryCSP.getKey()));
            String httpUrl = String
                    .format("%s?request=GetFeature&version=1.1.0&typeName=%s&maxFeatures=%d&startIndex=%d&outputFormat=application/json",
                            wfsEndpointUrl, layerName, limit, start);
            HttpEntity entity = new HttpEntity(headers);
            response = restTemplate.exchange(httpUrl, HttpMethod.GET, entity,
                    String.class);
            JSONObject jsonResul = (JSONObject) parser.parse(response.getBody());
            jsonResult.put(entryCSP.getKey(), jsonResul);
        }
        
        return (jsonResult.toJSONString());
    }
}
