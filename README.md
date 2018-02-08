# REST backend for the CLARUS protected data viewer


## Technical environment

The CLARUS Data Viewer backend is a Java-based application relying on the Spring framework (Spring Boot). Spring Boot allows to create web applications based on well-known Spring framework and embeds a server (Tomcat for example) easily. It allows to simplify Spring applications deployment.
Since the CLARUS Data Viewer is Java based, Maven is used as a project management tool based on POM (project object model), in order to manage project build and dependencies. SpringBoot is defined in Maven dependencies.

- Java
- Jersey
- SpringBoot (with embedded Jetty)
- Maven

## Launch the application

java -cp $classpath eu.clarussecure.dataviewer.Application

## Access path to resources

The CLARUS Data Viewer uses services based on REST (Representational State Transfer) technology, which are called RESTful web services. Those services are based on HTTP protocol and allow several operations such as GET, POST, PUT or DELETE (natively supported by HTTP).
In order to use RESTful web services in a more effective way, Jersey framework shall be used. This Jersey framework is supported natively by Spring Boot.

## Purpose (unique JSON representation for several data types and protocols)

The CLARUS protected data viewer  :
- easily adapts to different application domains (among which the eHealth, Geospatial)
- is able to process domain-specific data types : e.g. Geometries for the geospatial field 

To do so, it relies on a REST backend, namely the dataviewer_backend, which :
- expose as resources datasets stored in the cloud (e.g. patient passive data, vector geo data, etc.) accessed through the CLARUS proxy and security policies
- provide JSON representation of the resources; while allowing other representations if needed (e.g. content-type:image/jpeg, content-type:application/xml, etc.)

JSON is the preferred representation of the data viewer, whatever the protocol used to access the resource is (e.g. jdbc or http)

The REST interface is able to perform the following conversions:
- SQL to JSON (for DESCRIBE and SELECT)
- GML to JSON (for WFS DescribeFeatureType and GetFeature) 


## Jersey endpoints

- Security policy
dataviewer/security/policies
dataviewer/policy/{id}

- Describe
dataviewer/describe/pgsql/{table}
dataviewer/describe/wfs/{layer}

- Endpoints
dataviewer/endpoint/postgresql
dataviewer/endpoint/wfs

- Datasets
dataviewer/dataset/attributes/{policyname}
dataviewer/dataset/attributes/{policyname}

- Data
dataviewer/data/wfs/{layer}


## Extensibility

The dataviewer_backend is designed according to the Java extension mechanism, and thus can be extended e.g. for managing eHealth standards to access the data, Amazon S3 to access the data, image viewing (Raster) data via WMS, WMTS, WCS, etc.

CLARUS is an extensible application. That means CLARUS must be modify, upgrade, extend without change original code base. Thanks to this property, CLARUS should be enhance with new plug-ins or modules. However, this extensibility only allows to add new protocol to the proxy.

-	Service : Module which provide access to Data via some specific new protocol (ex : dataviewer-wfs, dataviewer-postgres, etc…)
-	ProtocolRessourceService : Class which implement ProtocolRessource (interface). This class defines method which allows to retrieve and add new protocol to CLARUS. Thanks to this class, original code is not modify.
-	ProtocolRessource (SPI) : Interface which defines classes and methods available to CLARUS application.

Each service (DataViewer-Protocol_X) implements ProtocolRessource interface. This interface declare some function to retrieve data (for example : “getProtectedData()”). Then, a service could define these override methods according to his own specificity.
ProtocolRessourceService class get particular attribute of ServiceLoader<ProtocolRessource> type. This attribute allows to realise iteration on interface implementation. That means for each service which implement ProtocolRessource, it is possible to acces to his proper methods (getProtectedData() in our example).
By this way, it’s possible to add all plug-in/module you need without modify original code.



