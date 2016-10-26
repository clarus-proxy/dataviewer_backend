package eu.clarussecure.dataviewer.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.clarussecure.dataviewer.model.ColumnInfo;
import eu.clarussecure.dataviewer.model.SecurityPolicyEndpoint;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Path("/describe")
public class DecribeDataResource {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * getPostgreSQLDescription
     * @param tableName
     * @return
     * @throws Exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/pgsql/{table}")
    public String getPostgreSQLDescription(@PathParam("table") String tableName) throws Exception {

        List<ColumnInfo> list = jdbcTemplate.query(String.format("SELECT column_name, data_type, udt_name, is_nullable FROM information_schema.columns WHERE table_name = '%s'; ", tableName), (ResultSet rs) -> {
            List<ColumnInfo> columnList = new ArrayList<ColumnInfo>();
            while (rs.next()) {
                ColumnInfo col = new ColumnInfo();
                col.setColumnName(rs.getString("column_name"));
                col.setDataType(rs.getString("data_type"));
                col.setUdtName(rs.getString("udt_name"));
                col.setNullable(rs.getBoolean("is_nullable"));
                columnList.add(col);
            }
            return columnList;
        });

        Gson gson = new Gson();

        return gson.toJson(list);
    }

    /**
     * getWFSDescription
     * @param layerName
     * @return
     * @throws Exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("wfs/{layer}")
    public String getWFSDescription(@PathParam("layer") String layerName) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        Gson gson = new Gson();

        String json = new EndpointListResource().getProtocolEndpoints("wfs");
        
        Type endpointListType = new TypeToken<ArrayList<SecurityPolicyEndpoint>>() {}.getType();
        List<SecurityPolicyEndpoint> wfsEndpoints = gson.fromJson(json, endpointListType);

        String wfsEndpointUrl = wfsEndpoints.stream().findFirst().orElse(null).getBaseUrl();

        String httpUrl = String.format("%s?request=DescribeFeatureType&version=1.1.0&typeName=%s&outputFormat=application/json", wfsEndpointUrl, layerName);

        return (restTemplate.getForObject(httpUrl, String.class));

    }

}
