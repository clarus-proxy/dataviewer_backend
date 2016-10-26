package eu.clarussecure.dataviewer;

import eu.clarussecure.dataviewer.config.JerseyConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Bean;

@EnableAutoConfiguration
public class RestApplication {
    
    /**
     * jerseyServlet
     * @return 
     */
    @Bean
    public ServletRegistrationBean jerseyServlet() {

        ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/dataviewer/*");
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());

        return registration;
    }

    /**
     * main
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(RestApplication.class).run(args);
    }
}
