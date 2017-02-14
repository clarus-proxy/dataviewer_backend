package eu.clarussecure.dataviewer.plugins.pgsql;

import eu.clarussecure.dataviewer.plugins.pgsql.util.Util;
import eu.clarussecure.dataviewer.spi.ProtocolResource;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PgsqlResource implements ProtocolResource {

    @Override
    public String getProtocolName() {
        return "PGSQL";
    }

	@Override
	public String getClearData(InetSocketAddress endpoint, String collectionName, String protocol, String server) {
		DataSource dataSource = Util.buildDataSource(Util.buildHttpUrl(endpoint, server));
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = String.format("SELECT * FROM %s" , collectionName);
	    JSONArray json = new JSONArray();
	    List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
	    results.forEach((result) -> {
	        JSONObject obj = new JSONObject();
	        result.entrySet().forEach((entry) -> {
	            obj.put(entry.getKey(), entry.getValue());
	        });
	        json.add(obj);
	    });

	    return json.toString();
	}

	@Override
	public String getProtectedData(InetSocketAddress endpoint, String collectionName, String protocol, String server) {
		DataSource dataSource = Util.buildDataSource(Util.buildHttpUrl(endpoint, server));
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String head = String
                .format("SELECT column_name, concat(table_name,'.',column_name) as protected_column_name FROM information_schema.columns WHERE table_name = '%s' ",
                		collectionName);
        List<Map<String, Object>> resultMetaData = jdbcTemplate.queryForList(head);
        JSONArray json = new JSONArray();
        TreeMap<Integer, String> cspList = new TreeMap<Integer, String>();
        resultMetaData.forEach((result) -> {
            result.entrySet().forEach((entry) -> {
            	Random rand = new Random();
                int cspKey = rand.nextInt(3) + 1;
                String value = (String) entry.getValue();
                if (entry.getKey().equals("protected_column_name")) {
                    value = "csp" + cspKey + "." + entry.getValue();
                    String cspValue = value.substring(0, 4);
                    cspList.put(cspKey, cspValue);
                }
            });
        });
        for (Map.Entry<Integer, String> entryCSP : cspList.entrySet()) {
        	String sql = String
                    .format("SELECT  CLARUS_PROTECTED(%d),*  FROM %s",
                            entryCSP.getKey(), collectionName);
            	sql = String.format("SELECT  *  FROM %s limit %s offset %d",
            			collectionName);
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            results.forEach((result) -> {
                JSONObject obj = new JSONObject();
                result.entrySet().forEach((entry) -> {
                    obj.put(entry.getKey(), entry.getValue());
                });
                json.add(obj);
            });
            json.add("end of csp");
        }
		return json.toString();
	}

}
