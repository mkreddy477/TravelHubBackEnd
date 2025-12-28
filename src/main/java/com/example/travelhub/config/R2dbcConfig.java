package com.example.travelhub.config;

import io.r2dbc.mssql.MssqlConnectionConfiguration;
import io.r2dbc.mssql.MssqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * R2DBC Configuration for MSSQL database connection
 */
@Configuration
@EnableR2dbcRepositories(basePackages = {"com.example.travelhub.flightbooking.repository", "com.example.travelhub.auth.repository"})
@EnableTransactionManagement
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.url}")
    private String url;

    @Value("${spring.r2dbc.username}")
    private String username;

    @Value("${spring.r2dbc.password}")
    private String password;

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        // Parse the R2DBC URL to extract host, port, and database
        // Format: r2dbc:mssql://host:port/database
        String connectionString = url.replace("r2dbc:mssql://", "");
        String[] parts = connectionString.split("/");
        String hostPort = parts[0];
        String database = parts.length > 1 ? parts[1] : "TravelHub";
        
        String[] hostParts = hostPort.split(":");
        String host = hostParts[0];
        int port = hostParts.length > 1 ? Integer.parseInt(hostParts[1]) : 1433;

        return new MssqlConnectionFactory(
                MssqlConnectionConfiguration.builder()
                        .host(host)
                        .port(port)
                        .database(database)
                        .username(username)
                        .password(password)
                        .build()
        );
    }

    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}
