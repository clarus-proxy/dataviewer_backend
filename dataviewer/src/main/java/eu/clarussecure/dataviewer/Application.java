package eu.clarussecure.dataviewer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            usage();
            //return;
        }
        new Application().configure(new SpringApplicationBuilder(Application.class)).run(args);
    }


    private static void usage() {

        System.out.println(
                "usage: java -Djava.ext.dirs=<CLARUS_EXT_DIRS> -jar dataviewer-0.0.1-SNAPSHOT.jar [OPTION]");
        System.out.println("");
        System.out.println(
                "  <CLARUS_EXT_DIRS>        list the extensions directories that contain protocol plugins for the dataviewer");
        System.out.println("Security policy options:");
        System.out.println(" --security-policy=<PATH_TO_SECURITY_POLICY>");
        System.out.println("                           the security policy to apply");
        System.out.println("Proxy-Host mapping options:");
        System.out.println(" --proxyhost-mapping=<PATH_TO_PROXY_HOST_MAPPING_FILE>");
        System.out.println("                           mapping between security policies and running proxies");

    }
}
