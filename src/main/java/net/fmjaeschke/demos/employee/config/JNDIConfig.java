package net.fmjaeschke.demos.employee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jndi.JndiTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:persistence-jndi.properties")
public class JNDIConfig {

    private final Environment environment;

    @Inject
    public JNDIConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Profile("webcontainer")
    public DataSource dataSource() throws NamingException {
        return (DataSource) new JndiTemplate().lookup(environment.getProperty("jdbc.url"));
    }
}
