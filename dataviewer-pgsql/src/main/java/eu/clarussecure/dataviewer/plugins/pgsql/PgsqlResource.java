package eu.clarussecure.dataviewer.plugins.pgsql;

import eu.clarussecure.dataviewer.spi.ProtocolResource;
import org.springframework.stereotype.Component;

@Component
public class PgsqlResource implements ProtocolResource {

    @Override
    public String getProtocolName() {
        return "PGSQL";
    }

    @Override
    public String getClearData(String protocol, String collectionName) {

        return "PGSQL clear data";

    }

    @Override
    public String getProtectedData(String protocol, String collectionName) {

        return "PGSQL protected data";

    }


}
