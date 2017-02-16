package eu.clarussecure.dataviewer.resources;

import eu.clarussecure.dataviewer.spi.ProtocolResourceService;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/lookup")
public class ProtocolResourceLookup {

    /*
     * TODO way to retrieve endpoint (host + port) is actually not define. That is why
     * both var are declared null in order to be able to compile project
     */
    protected InetSocketAddress endpoint = null;
    protected String store = "";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/data/protected/{protocol}/{collection}")
    public String getProtectedData(@PathParam("protocol") String protocol, @PathParam("collection") String collection) {

        ProtocolResourceService service = ProtocolResourceService.getInstance();

        String protectedData = service.getProtectedData(protocol, getEndpoint(), getStore(), collection);

        return protectedData;

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/data/{protocol}/{collection}")
    public String getClearData(@PathParam("protocol") String protocol, @PathParam("collection") String collection) {

        ProtocolResourceService service = ProtocolResourceService.getInstance();

        String clearData = service.getClearData(protocol, getEndpoint(), getStore(), collection);

        return clearData;

    }


    public InetSocketAddress getEndpoint(){
        return endpoint;
    }

    public String getStore(){
        return store;
    }



}
