/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.clarussecure.dataviewer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * @author mark.burville
 *
 */
@SpringBootApplication
//@ImportResource({ "classpath:platform-client-context.xml" })
public class Application extends SpringBootServletInitializer {

    /**
     *
     *
     * @param application
     */
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    /**
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        new Application().configure(new SpringApplicationBuilder(Application.class)).run(args);
    }
}
