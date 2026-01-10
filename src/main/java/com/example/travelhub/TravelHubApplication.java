package com.example.travelhub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TravelHubApplication {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(TravelHubApplication.class, args);
        // YOUR SQL SERVER CONNECTION URL
        // Option A: SQL Server Authentication (username/password)
        String url = "jdbc:sqlserver://localhost;" +
                "databaseName=TravelHub;" +
                "user=sa;" +
                "password=Travelhub@123;" +
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
}
