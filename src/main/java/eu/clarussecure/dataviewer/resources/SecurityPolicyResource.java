package eu.clarussecure.dataviewer.resources;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Path("/security")
public class SecurityPolicyResource {

    /**
     * getPolicies
     * @return
     * @throws IOException
     * @throws ParseException 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/policies")
    public String getPolicies() throws IOException, ParseException {

        return getPolicyList().toJSONString();

    }

    /**
     * getPolicy
     * @param id
     * @return
     * @throws IOException
     * @throws ParseException 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/policy/{id}")
    public String getPolicy(@PathParam("id") int id) throws IOException, ParseException {

        return getPolicyList().get(id).toString();
    }

    /**
     * getPolicyList
     * @return
     * @throws IOException
     * @throws ParseException 
     */
    private JSONArray getPolicyList() throws IOException, ParseException {

        ClassPathResource policyFile = new ClassPathResource("securitypolicy-example.json");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(policyFile.getInputStream()));
        JSONArray policyList = (JSONArray) obj;
        
        return policyList;

    }
  
}
