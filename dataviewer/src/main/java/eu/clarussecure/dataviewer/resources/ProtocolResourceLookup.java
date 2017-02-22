package eu.clarussecure.dataviewer.resources;

import eu.clarussecure.dataviewer.spi.ProtocolResourceService;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/view")
public class ProtocolResourceLookup {

    /*
     * way to retrieve endpoint (host + port) is not defined yet.
     * temporarily set default values for testing
     * TODO remove test values
     */
    //protected InetSocketAddress endpoint = null;
    protected InetSocketAddress endpoint = new InetSocketAddress("10.15.0.89",5432);
    //protected String store = "";
    protected String store = "ehealth";

    private ProtocolResourceService service = ProtocolResourceService.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/data/protected/{protocol}/{collection}")
    public String getProtectedData(@PathParam("protocol") String protocol, @PathParam("collection") String collection,
                                   @QueryParam("limit") @DefaultValue("all") String limit,
                                   @QueryParam("start") @DefaultValue("0") String start) {

        String protectedData = service.getProtectedData(protocol, getEndpoint(), getStore(), collection, limit, start);

        return protectedData;

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/data/{protocol}/{collection}")
    public String getClearData(@PathParam("protocol") String protocol, @PathParam("collection") String collection,
                               @QueryParam("limit") @DefaultValue("all") String limit,
                               @QueryParam("start") @DefaultValue("0") String start) {

        String clearData = service.getClearData(protocol, getEndpoint(), getStore(), collection, limit, start);

        return clearData;

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/description/{protocol}/{collection}")
    public String getDescription(@PathParam("protocol") String protocol, @PathParam("collection") String collection) {

        String description = service.getDescription(protocol, getEndpoint(), getStore(), collection);

        return description;

    }


    public InetSocketAddress getEndpoint(){
        return endpoint;
    }

    public String getStore(){
        return store;
    }



}
