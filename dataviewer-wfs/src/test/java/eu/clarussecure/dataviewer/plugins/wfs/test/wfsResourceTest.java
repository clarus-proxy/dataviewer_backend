package eu.clarussecure.dataviewer.plugins.wfs.test;

import java.net.InetSocketAddress;

import org.junit.Assert;
import org.junit.Test;

import eu.clarussecure.dataviewer.plugins.wfs.WfsResource;


public class wfsResourceTest {

    final String hostName = "10.15.0.91";
    final String hostNameProtected = "10.15.0.91";
    final int port = 8080;
    final String store = "geoserver/clarus";
    final String protocol = "wfs";
    final String limit = "500";
    final String start = "0";

    @Test
    public void testRestTemplate() {
        WfsResource resource = new WfsResource();
        InetSocketAddress endpoint = new InetSocketAddress(hostName, port);
        String data = resource.getClearData(protocol, endpoint, store, "groundwater_boreholes", limit, start);
        Assert.assertNotNull("Request shall retrieve some data", data);
    }
    
    @Test
	public void wfsDescriptionTest(){
    	WfsResource  resource = new WfsResource();
		InetSocketAddress endpoint = new InetSocketAddress(hostNameProtected, port);
		String data = resource.getDescription(protocol, endpoint, store,"groundwater_boreholes");
		Assert.assertNotSame("Request shall retrieve some informations and not XML error", "<", data.substring(0, 1));
		Assert.assertEquals("Request shall retrieve some informations aas JSON format", "{", data.substring(0, 1));
	}

}
