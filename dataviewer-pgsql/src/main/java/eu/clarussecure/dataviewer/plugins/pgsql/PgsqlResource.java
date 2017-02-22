package eu.clarussecure.dataviewer.plugins.pgsql;

import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import eu.clarussecure.dataviewer.plugins.pgsql.model.ColumnInfo;
import eu.clarussecure.dataviewer.plugins.pgsql.util.Util;
import eu.clarussecure.dataviewer.spi.ProtocolResource;

@Component
public class PgsqlResource implements ProtocolResource {

    @Override
    public String getProtocolName() {
        return "PGSQL";
    }

    @Override
    public String getClearData(String protocol, InetSocketAddress endpoint, String store, String collection, String limit, String start) {

        DataSource dataSource = Util.buildDataSource(Util.buildHttpUrl(endpoint, store));
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = String.format("SELECT * FROM %s", collection);
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
    public String getProtectedData(String protocol, InetSocketAddress endpoint, String store, String collection, String limit, String start) {

        DataSource dataSource = Util.buildDataSource(Util.buildHttpUrl(endpoint, store));
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String head = String
                .format("SELECT column_name, concat(table_name,'.',column_name) as protected_column_name FROM information_schema.columns WHERE table_name = '%s' ",
                        collection);
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
                            entryCSP.getKey(), collection);
            sql = String.format("SELECT  *  FROM %s limit %s offset %s",
                    collection, limit, start);
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

    @Override
    public String getDescription(String protocol, InetSocketAddress endpoint, String store, String collection) {

        DataSource dataSource = Util.buildDataSource(Util.buildHttpUrl(endpoint, store));
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<ColumnInfo> list = jdbcTemplate.query(String.format("SELECT column_name, data_type, udt_name, is_nullable FROM information_schema.columns WHERE table_name = '%s'; ",
                collection), (ResultSet rs) -> {
            List<ColumnInfo> columnList = new ArrayList<ColumnInfo>();
            while (rs.next()) {
                ColumnInfo col = new ColumnInfo();
                col.setName(rs.getString("column_name"));
                col.setType(rs.getString("data_type"));
                col.setUdtName(rs.getString("udt_name"));
                col.setNullable(rs.getBoolean("is_nullable"));
                columnList.add(col);
            }
            return columnList;
        });
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
