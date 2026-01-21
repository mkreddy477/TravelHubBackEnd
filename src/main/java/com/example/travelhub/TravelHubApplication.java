package com.example.travelhub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableCaching
public class TravelHubApplication {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(TravelHubApplication.class, args);
        // YOUR SQL SERVER CONNECTION URL
        String url = "jdbc:sqlserver://localhost;" +
                "databaseName=TravelHub;" +
                "integratedSecurity=true;" +
                "encrypt=true;" +
                "trustServerCertificate=true;";
        System.out.println("Testing connection to: " + url);
        System.out.println("Connecting...\n");
        
        try {
            Connection conn = DriverManager.getConnection(url);
            
            if (conn != null) {
                System.out.println("✓✓✓ SUCCESS! DATABASE IS CONNECTED ✓✓✓\n");
                
                // Show some info
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT DB_NAME() AS DB, @@VERSION AS Ver");
                
                if (rs.next()) {
                    System.out.println("Connected to database: " + rs.getString("DB"));
                    System.out.println("SQL Server version: " + rs.getString("Ver").substring(0, 50) + "...");
                }
                
                conn.close();
                System.out.println("\n✓ Connection test PASSED!");
            }
            
        } catch (SQLException e) {
            System.out.println("✗✗✗ FAILED! CANNOT CONNECT ✗✗✗\n");
            System.out.println("Error: " + e.getMessage());
            System.out.println("\nPossible issues:");
            System.out.println("1. SQL Server not running");
            System.out.println("2. Wrong database name (try 'master' instead of 'TestDB')");
            System.out.println("3. JDBC driver not in classpath");
            System.out.println("4. Network/firewall blocking port 1433");
        }

    }

    @Slf4j
    @Component
    public static class StartupLogger {
        
        private final Environment environment;
        
        public StartupLogger(Environment environment) {
            this.environment = environment;
        }
        
        @EventListener(ApplicationReadyEvent.class)
        public void onApplicationReady() {
            String port = environment.getProperty("server.port", "8080");
            
                  }
    }
}
