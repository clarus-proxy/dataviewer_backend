package eu.clarussecure.dataviewer.spi;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.ServiceLoader;

public class ProtocolResourceService {

    private static ProtocolResourceService service;
    private ServiceLoader<ProtocolResource> loader;

    private ProtocolResourceService() {
        loader = ServiceLoader.load(ProtocolResource.class);
    }

    public static synchronized ProtocolResourceService getInstance() {

        if (service == null) {
            service = new ProtocolResourceService();
        }
        return service;
    }

    private ProtocolResource getProtocolResourceProvider(String protocol) {

        Iterator<ProtocolResource> resources = loader.iterator();
        while (resources.hasNext()) {
            ProtocolResource pluginResource = resources.next();
            if (pluginResource.getClass().getSimpleName().toLowerCase().contains(protocol.toLowerCase())) {
                return pluginResource;
            }
        }

        return null;
    }

    public String getProtectedData(String protocol, InetSocketAddress endpoint, String store, String collection, String limit, String start) {

        String protectedData = null;

        ProtocolResource protocolResourceProvider = getProtocolResourceProvider(protocol);
        if (protocolResourceProvider != null) {
            protectedData = protocolResourceProvider.getProtectedData(protocol, endpoint, store, collection, limit, start);
        }

        return protectedData;
    }


    public String getClearData(String protocol, InetSocketAddress endpoint, String store, String collection, String limit, String start) {

        String clearData = null;

        ProtocolResource protocolResourceProvider = getProtocolResourceProvider(protocol);
        if (protocolResourceProvider != null) {
            clearData = protocolResourceProvider.getClearData(protocol, endpoint, store, collection, limit, start);
        }

        return clearData;
    }

    public String getDescription(String protocol, InetSocketAddress endpoint, String store, String collection) {

        String description = null;

        ProtocolResource protocolResourceProvider = getProtocolResourceProvider(protocol);
        if (protocolResourceProvider != null) {
            description = protocolResourceProvider.getDescription(protocol, endpoint, store, collection);
        }

        return description;
    }


}
