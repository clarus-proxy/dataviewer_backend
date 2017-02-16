package UnitTest;

import java.net.InetSocketAddress;

import org.junit.Assert;
import org.junit.Test;

import eu.clarussecure.dataviewer.plugins.pgsql.PgsqlResource;


public class pgsqlResourceTest {

	final String hostName = "10.15.0.89";
	final String hostNameProtected = "10.15.0.89";
	final int port = 5432;
	final String store = "ehealth";
	final String protocol = "pgsql";

	@Test
	public void pgsqlClearTest() {
		PgsqlResource  resource = new PgsqlResource();
		InetSocketAddress endpoint = new InetSocketAddress(hostName, port);
		String data = resource.getClearData(protocol, endpoint, store, "patient");
		Assert.assertNotNull("Request shall retrieve some data", data);
	}
	
	@Test
	public void pgsqlProtectedTest(){
		PgsqlResource  resource = new PgsqlResource();
		InetSocketAddress endpoint = new InetSocketAddress(hostNameProtected, port);
		String data = resource.getProtectedData(protocol, endpoint, store,"patient");
		Assert.assertNotNull("Request shall retrieve some data", data);
	}
	
}
