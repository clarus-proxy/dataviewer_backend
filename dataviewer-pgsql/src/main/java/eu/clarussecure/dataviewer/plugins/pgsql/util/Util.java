package eu.clarussecure.dataviewer.plugins.pgsql.util;

import java.net.InetSocketAddress;

import org.apache.tomcat.jdbc.pool.DataSource;

public class Util {

	public static DataSource buildDataSource(String url){
		String user = "postgres";
		String password = "postgres";
	    DataSource dataSource = new DataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		return dataSource;
	}
	
	public static String buildHttpUrl(InetSocketAddress endpoint, String dataSourceName){
		String host = endpoint.getHostName();
		String port = String.valueOf(endpoint.getPort());
		String url = "jdbc:postgresql://" + host + ":" + port + "/" + dataSourceName;
		return url;
	}
}
