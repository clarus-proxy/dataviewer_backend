package eu.clarussecure.dataviewer.plugins.pgsql.test;

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
	final String limit = "25";
	final String start = "0";

	@Test
	public void pgsqlClearTest() {
		PgsqlResource  resource = new PgsqlResource();
		InetSocketAddress endpoint = new InetSocketAddress(hostName, port);
		String data = resource.getClearData(protocol, endpoint, store, "patient", limit, start);
		Assert.assertNotNull("Request shall retrieve some data", data);
	}
	
	@Test
	public void pgsqlProtectedTest(){
		PgsqlResource  resource = new PgsqlResource();
		InetSocketAddress endpoint = new InetSocketAddress(hostNameProtected, port);
		String data = resource.getProtectedData(protocol, endpoint, store,"patient", limit, start);
		Assert.assertNotNull("Request shall retrieve some data", data);
	}
	
	@Test
	public void pgsqlDescriptionTest(){
		PgsqlResource  resource = new PgsqlResource();
		InetSocketAddress endpoint = new InetSocketAddress(hostNameProtected, port);
		String data = resource.getDescription(null, endpoint, store,"patient");
		Assert.assertNotEquals("Request shall retrieve some informations", "[]",data);
	}
	
}
