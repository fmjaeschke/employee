package net.fmjaeschke.demos.employee.config;

import net.fmjaeschke.demos.employee.rest.Employees;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    private static final Logger logger = LoggerFactory.getLogger(JerseyConfig.class);

    public JerseyConfig() {
        try {
            register(Employees.class);
            property(ServletProperties.FILTER_FORWARD_ON_404, true);

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }
}
