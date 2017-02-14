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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/data/{protocol}/{collection}")
    public String getProtectedData(@PathParam("protocol") String protocol, @PathParam("collection") String collectionName) {

        ProtocolResourceService service = ProtocolResourceService.getInstance();
        /*
         * TODO way to retrieve endpoint (host + port) is actually not define. That is why 
         * both var are declared null in order to be able to compile project
         */
        InetSocketAddress endpoint = null;
        String server = "";
        String protectedData = service.getProtectedData(endpoint, collectionName, protocol, server);

        return protectedData;

    }


}
