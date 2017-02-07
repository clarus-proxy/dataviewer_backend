package eu.clarussecure.dataviewer.resources;

import eu.clarussecure.dataviewer.spi.ProtocolResourceService;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/test")
public class ProtocolResourceLookup {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/data/{protocol}/{collection}")
    public String getProtectedData(@PathParam("protocol") String protocol, @PathParam("collection") String collectionName) {

        ProtocolResourceService service = ProtocolResourceService.getInstance();

        String protectedData = service.getProtectedData(protocol, collectionName);

        return protectedData;

    }


}
