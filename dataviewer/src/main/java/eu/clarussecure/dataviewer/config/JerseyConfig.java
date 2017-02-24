package eu.clarussecure.dataviewer.config;

import eu.clarussecure.dataviewer.resources.*;

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
        register(CapabilitiesResource.class);

        register(ProtocolResourceLookup.class);

    }
    
}
