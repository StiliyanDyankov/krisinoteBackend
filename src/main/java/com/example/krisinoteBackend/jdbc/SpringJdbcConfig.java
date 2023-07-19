package com.example.krisinoteBackend.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.example.krisinoteBackend")
public class SpringJdbcConfig {
//
    @Value("${SPRING_DATASOURCE_URL}")
    private String SPRING_DATASOURCE_URL;
    @Value("${SPRING_DATASOURCE_USERNAME}")
    private String SPRING_DATASOURCE_USERNAME;
    @Value("${SPRING_DATASOURCE_PASSWORD}")
    private String SPRING_DATASOURCE_PASSWORD;
//    @Bean
//    public DataSource mysqlDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUrl(SPRING_DATASOURCE_URL);
//        dataSource.setUsername(SPRING_DATASOURCE_USERNAME);
//        dataSource.setPassword(SPRING_DATASOURCE_PASSWORD);
//
//        return dataSource;
//    }
//    @Bean
//    public JdbcTemplate jdbcTemplate(HikariDataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//
    @Bean
    @Primary
    @ConfigurationProperties("app.datasource")
    public HikariDataSource hikariDataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(SPRING_DATASOURCE_URL);
//        config.setUsername(SPRING_DATASOURCE_USERNAME);
//        config.setPassword(SPRING_DATASOURCE_PASSWORD);
//        config.setMaximumPoolSize(30);

        return DataSourceBuilder
                .create()
                .type(HikariDataSource.class)
                .build();
//        return new HikariDataSource(config);
    }
}


