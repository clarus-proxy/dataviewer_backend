package eu.clarussecure.dataviewer.spi;

import java.net.InetSocketAddress;

public interface ProtocolResource {

    String getProtocolName();

    String getClearData(InetSocketAddress endpoint, String collectionName, String protocol, String server);

    String getProtectedData(InetSocketAddress endpoint, String collectionName, String protocol, String server);



}
