package eu.clarussecure.dataviewer.config;

import eu.clarussecure.dataviewer.resources.DatasetListResource;
import eu.clarussecure.dataviewer.resources.SecurityPolicyResource;
import eu.clarussecure.dataviewer.resources.DecribeDataResource;
import eu.clarussecure.dataviewer.resources.EndpointListResource;
import eu.clarussecure.dataviewer.resources.DataRessource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;


@Component
public class JerseyConfig extends ResourceConfig {
    
    /**
     * JerseyConfig
     */
    public JerseyConfig(){        
        // register REST resources
        register(SecurityPolicyResource.class);
        register(DatasetListResource.class);
        register(DecribeDataResource.class);
        register(EndpointListResource.class);
        register(DataRessource.class);
    }
    
}
