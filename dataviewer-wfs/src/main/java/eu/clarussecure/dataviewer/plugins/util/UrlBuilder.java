package eu.clarussecure.dataviewer.plugins.util;

import java.net.InetSocketAddress;

public class UrlBuilder {

	public static String buildHttpUrl(InetSocketAddress endpoint){
		String host = endpoint.getHostName();
		String port = String.valueOf(endpoint.getPort());
		String url = "http://" + host + ":" + port;
		return url;
	}
}
