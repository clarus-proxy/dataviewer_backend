package UnitTest;
import java.net.InetSocketAddress;

import org.junit.Assert;
import org.junit.Test;

import eu.clarussecure.dataviewer.plugins.wfs.WfsResource;

public class WfsRessourceTest {

	final String hostName = "10.15.0.91";
	final String hostNameProtected = "localhost";
	final int port = 8080;
	final String server = "geoserver";
	final String protocol = "wfs";
	
	@Test
	public void wtfClearTest() {
		WfsResource  resource = new WfsResource();
		InetSocketAddress endpoint = new InetSocketAddress(hostName, port);
		String data = resource.getClearData("groundwater_boreholes", endpoint, server, protocol);
		Assert.assertEquals("Data shall be not null and return Json", "{", String.valueOf(data.charAt(0)));
	}
	
	@Test
	public void wfsProtectedTest() {
		WfsResource  resource = new WfsResource();
		InetSocketAddress endpoint = new InetSocketAddress(hostNameProtected, port);
		String data = resource.getProtectedData("groundwater_boreholes", endpoint, server, protocol);
		Assert.assertEquals("Data shall be not null and return Json", "{", String.valueOf(data.charAt(0)));
	}

}
