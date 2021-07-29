package net.fmjaeschke.demos.employee.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class LocalConfig {

    @Bean
    @Profile("default")
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .username("lotte")
                .password("h4mS3KGWIrrWVgGvO5cp")
                .url("jdbc:postgresql://Calisto:5432/lotte")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
