package eu.clarussecure.dataviewer.spi;

import java.net.InetSocketAddress;

public interface ProtocolResource {

    String getProtocolName();

    String getClearData(String protocol, InetSocketAddress endpoint, String store, String collection, String limit, String start);

    String getProtectedData(String protocol, InetSocketAddress endpoint, String store, String collection, String limit, String start);

    String getDescription(String protocol, InetSocketAddress endpoint, String store, String collection);

}
