package eu.clarussecure.dataviewer.config;

import eu.clarussecure.dataviewer.resources.ProtocolResourceLookup;
import eu.clarussecure.dataviewer.resources.CountDataResource;
import eu.clarussecure.dataviewer.resources.DatasetListResource;
import eu.clarussecure.dataviewer.resources.SecurityPolicyResource;
import eu.clarussecure.dataviewer.resources.DecribeDataResource;
import eu.clarussecure.dataviewer.resources.EndpointListResource;
import eu.clarussecure.dataviewer.resources.DataResource;
import eu.clarussecure.dataviewer.resources.SchemaResource;

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
        register(DataResource.class);
        register(SchemaResource.class);
        register(CountDataResource.class);

        register(ProtocolResourceLookup.class);

    }
    
}
