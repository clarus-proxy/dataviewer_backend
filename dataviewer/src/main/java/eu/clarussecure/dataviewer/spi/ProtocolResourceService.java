package eu.clarussecure.dataviewer.spi;


import java.util.Iterator;
import java.util.ServiceLoader;


public class ProtocolResourceService {

    private static ProtocolResourceService service;
    private ServiceLoader<ProtocolResource> loader;

    private ProtocolResourceService() {
        loader = ServiceLoader.load(ProtocolResource.class);
    }

    public static synchronized ProtocolResourceService getInstance() {

        if (service == null){
            service = new ProtocolResourceService();
        }
        return service;
    }


    public String getProtectedData(String protocol, String collectionName){

        String protectedData = null;

        Iterator<ProtocolResource> resources = loader.iterator();
        while (resources.hasNext()){
            ProtocolResource pluginResource = resources.next();
            if (pluginResource.getClass().getSimpleName().toLowerCase().contains(protocol.toLowerCase())) {
                protectedData = pluginResource.getProtectedData(protocol, collectionName);
            }
        }

        return protectedData;
    }

}
