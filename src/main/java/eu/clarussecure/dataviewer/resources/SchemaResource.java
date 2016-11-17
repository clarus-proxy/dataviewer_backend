package eu.clarussecure.dataviewer.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.clarussecure.dataviewer.model.SecurityPolicy;
import eu.clarussecure.dataviewer.model.SecurityPolicyAttribute;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Path("/schema")
public class SchemaResource {
       
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/policy/{id}")
    public Set getSchema(@PathParam("id") int id) throws IOException {
        
        Gson gson = new Gson();
        ClassPathResource policyFile = new ClassPathResource("securitypolicy-example.json");
        
        List<SecurityPolicy> policies = gson.fromJson(new InputStreamReader(policyFile.getInputStream()), new TypeToken<ArrayList<SecurityPolicy>>(){}.getType());
        
        Set<String> attributePaths = new HashSet();
        
        /*
        for (SecurityPolicy policy : policies){
        if (policy.getPolicyId() == id){
        for (SecurityPolicyAttribute attribute : policy.getAttributes()){
        String path = attribute.getPath();
        int firstSlash = path.indexOf("/");
        int secondSlash = path.indexOf("/", firstSlash + 1);
        String node = path.substring(firstSlash + 1, secondSlash);
        attributePaths.add(node);
        }
        }
        }
         */
        
        policies.stream().filter((policy) -> (policy.getPolicyId() == id)).forEachOrdered((policy) -> {
            policy.getAttributes().stream().map((attribute) -> attribute.getPath()).map((path) -> {
                int firstSlash = path.indexOf("/");
                int secondSlash = path.indexOf("/", firstSlash + 1);
                String node = path.substring(firstSlash + 1, secondSlash);
                return node;
            }).forEachOrdered((node) -> {
                attributePaths.add(node);
            });
        });
 
        return attributePaths;
        
    }
    
}
