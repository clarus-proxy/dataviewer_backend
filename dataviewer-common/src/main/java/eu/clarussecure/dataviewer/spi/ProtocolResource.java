package eu.clarussecure.dataviewer.spi;


public interface ProtocolResource {

    String getProtocolName();

    String getClearData(String protocol, String CollectionName);

    String getProtectedData(String protocol, String CollectionName);


}
