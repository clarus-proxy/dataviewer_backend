package eu.clarussecure.dataviewer.plugins.wfs;


import eu.clarussecure.dataviewer.spi.ProtocolResource;

public class WfsResource implements ProtocolResource {

    @Override
    public String getProtocolName() {
        return "WFS";
    }

    @Override
    public String getClearData(String protocol, String collectionName) {
        return "WFS clear data";
    }

    @Override
    public String getProtectedData(String protocol, String collectionName) {
        return "WFS protected data";
    }

}
