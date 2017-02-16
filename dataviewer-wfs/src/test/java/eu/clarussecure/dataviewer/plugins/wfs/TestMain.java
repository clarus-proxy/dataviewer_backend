package eu.clarussecure.dataviewer.plugins.wfs;

import eu.clarussecure.dataviewer.plugins.wfs.WfsResource;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetSocketAddress;


public class TestMain {

    final String hostName = "10.15.0.91";
    final String hostNameProtected = "10.15.0.91";
    final int port = 8080;
    final String store = "geoserver/clarus";
    final String protocol = "wfs";

    @Test
    public void testRestTemplate() {

        WfsResource resource = new WfsResource();
        InetSocketAddress endpoint = new InetSocketAddress(hostName, port);
        String data = resource.getClearData(protocol, endpoint, store, "groundwater_boreholes");
        Assert.assertNotNull("Request shall retrieve some data", data);


    }

}
