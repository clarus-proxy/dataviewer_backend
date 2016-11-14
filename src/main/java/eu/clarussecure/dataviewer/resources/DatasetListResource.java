package eu.clarussecure.dataviewer.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.clarussecure.dataviewer.model.DatasetDescription;
import eu.clarussecure.dataviewer.model.SecurityPolicy;
import eu.clarussecure.dataviewer.model.SecurityPolicyAttribute;
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
@Path("/dataset")
public class DatasetListResource {

    /**
     * getProtocolEndpoints
     * @param policyName
     * @return
     * @throws IOException
     * @throws ParseException 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/attributes/{policyname}")
    public String getPolicyAttributes(@PathParam("policyname") String policyName) throws IOException, ParseException {

        ClassPathResource policyFile = new ClassPathResource("securitypolicy-example.json");
        
        Gson gson = new Gson();
            
        List<SecurityPolicy> policies = gson.fromJson(new InputStreamReader(policyFile.getInputStream()), new TypeToken<ArrayList<SecurityPolicy>>() {}.getType());

        SecurityPolicy sp = policies.stream().filter(policy -> policy.getPolicyName().equals(policyName)).findFirst().orElse(new SecurityPolicy());
        
        return gson.toJson(sp.getAttributes(), new TypeToken<ArrayList<SecurityPolicyAttribute>>() {}.getType());

    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public String getDatasetList() throws IOException {
        
        
        Gson gson = new Gson();
        ClassPathResource policyFile = new ClassPathResource("securitypolicy-example.json");
        
        List<DatasetDescription> datasetDescriptions = new ArrayList<DatasetDescription>();
               
        List<SecurityPolicy> policies = gson.fromJson(new InputStreamReader(policyFile.getInputStream()), new TypeToken<ArrayList<SecurityPolicy>>(){}.getType());
        
        // policies.stream().filter(policy -> policy.getEndpoint().getProtocol().equals(protocolName)).map(SecurityPolicy::getEndpoint);
        for (SecurityPolicy policy : policies){
            
            DatasetDescription datasetDescription = new DatasetDescription();
            datasetDescription.setName(policy.getPolicyName());
            datasetDescription.setProtocol(policy.getEndpoint().getProtocol());
            datasetDescription.setTechnique(policy.getProtection().getModule());
            datasetDescriptions.add(datasetDescription);
            
        }

        return gson.toJson(datasetDescriptions, new TypeToken<ArrayList<DatasetDescription>>(){}.getType());

    }


}
