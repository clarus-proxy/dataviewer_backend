package eu.clarussecure.dataviewer.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.clarussecure.dataviewer.model.SecurityPolicy;
import eu.clarussecure.dataviewer.model.SecurityPolicyEndpoint;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Path("/endpoint")
public class EndpointListResource {
    
    /**
     * getProtocolEndpoints
     * @param protocolName
     * @return
     * @throws IOException
     * @throws ParseException 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{protocol}")
    public String getProtocolEndpoints(@PathParam("protocol") String protocolName) throws IOException, ParseException {

        Gson gson = new Gson();
        ClassPathResource policyFile = new ClassPathResource("securitypolicy-example.json");
        List<SecurityPolicyEndpoint> protocolEndpoints = new ArrayList<SecurityPolicyEndpoint>();
               
        List<SecurityPolicy> policies = gson.fromJson(new InputStreamReader(policyFile.getInputStream()), new TypeToken<ArrayList<SecurityPolicy>>(){}.getType());
        
        // policies.stream().filter(policy -> policy.getEndpoint().getProtocol().equals(protocolName)).map(SecurityPolicy::getEndpoint);
        for (SecurityPolicy policy : policies){
            if (policy.getEndpoint().getProtocol().equals(protocolName)){
                protocolEndpoints.add(policy.getEndpoint());
            }
        }

        return gson.toJson(protocolEndpoints, new TypeToken<ArrayList<SecurityPolicyEndpoint>>(){}.getType());

    }

}
