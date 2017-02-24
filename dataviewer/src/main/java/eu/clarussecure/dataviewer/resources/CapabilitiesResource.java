package eu.clarussecure.dataviewer.resources;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Component
@Path("/capabilities")
public class CapabilitiesResource {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/pgsql/{schema}")
    public String getPSQLCapabilities(@PathParam("schema") String schema) throws Exception {

        String sql = String.format("SELECT * FROM pg_tables where schemaname='%s';", schema);
        JSONArray json = new JSONArray();

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        json.add(results);

        return json.toString();
    }



}
