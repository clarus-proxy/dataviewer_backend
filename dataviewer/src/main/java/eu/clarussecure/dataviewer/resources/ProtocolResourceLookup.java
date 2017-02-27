package eu.clarussecure.dataviewer.resources;

import eu.clarussecure.dataviewer.spi.ProtocolResourceService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/{proxyid}")
public class ProtocolResourceLookup {

    @PathParam("proxyid")
    private String proxyId;

    private static final String URL_PROXY_PATTERN = "proxy";

    private ProtocolResourceService service = ProtocolResourceService.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{protocol}/{collection}/protected")
    public String getProtectedData(@PathParam("protocol") String protocol, @PathParam("collection") String collection,
                                   @QueryParam("limit") @DefaultValue("all") String limit,
                                   @QueryParam("start") @DefaultValue("0") String start) {

        String protectedData = service.getProtectedData(protocol, getRunningProxyAddress(getProxyId()), getStore(getProxyId()), collection, limit, start);

        return protectedData;

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{protocol}/{collection}")
    public String getClearData(@PathParam("protocol") String protocol, @PathParam("collection") String collection,
                               @QueryParam("limit") @DefaultValue("all") String limit,
                               @QueryParam("start") @DefaultValue("0") String start) {

        String clearData = service.getClearData(protocol, getRunningProxyAddress(getProxyId()), getStore(getProxyId()), collection, limit, start);

        return clearData;

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/description/{protocol}/{collection}")
    public String getDescription(@PathParam("protocol") String protocol, @PathParam("collection") String collection) {

        String description = service.getDescription(protocol, getRunningProxyAddress(getProxyId()), getStore(getProxyId()), collection);

        return description;

    }

    /**
     * getProxyId
     *
     * @return id
     */
    private int getProxyId() {
        int pos = URL_PROXY_PATTERN.length();
        return Integer.parseInt(proxyId.substring(pos, pos + 1));
    }

    /**
     * getRunningProxyAddress
     *
     * @param id
     * @return endpoint
     * @throws IOException
     * @throws ParseException
     */
    private InetSocketAddress getRunningProxyAddress(int id) {

        InetSocketAddress endpoint = null;
        JSONParser parser = new JSONParser();
        ClassPathResource proxyMappingFile = new ClassPathResource("proxyhostmapping-example.json");

        JSONArray array = null;
        try {
            array = (JSONArray) (parser.parse(new InputStreamReader(proxyMappingFile.getInputStream())));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Object object : array) {
            String policyId = ((JSONObject) object).get("policyId").toString();
            if (Integer.parseInt(policyId) == id) {
                endpoint = new InetSocketAddress(((JSONObject) object).get("proxyHost").toString(), Integer.parseInt(((JSONObject) object).get("proxyPort").toString()));
            }
        }

        return endpoint;
    }


    /**
     * getStore
     * @param id
     * @return store
     */
    private String getStore(int id) {

        String store = null;
        JSONParser parser = new JSONParser();
        ClassPathResource policyFile = new ClassPathResource("securitypolicy-example.json");

        JSONArray array = null;

        try {
            array = (JSONArray) (parser.parse(new InputStreamReader(policyFile.getInputStream())));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Object object : array) {
            String policyId = ((JSONObject) object).get("policyId").toString();
            if (Integer.parseInt(policyId) == id) {
                JSONObject endpoint = (JSONObject)((JSONObject) object).get("endpoint");
                store = (String) endpoint.get("basePath");
            }
        }

        return store;

    }


}
