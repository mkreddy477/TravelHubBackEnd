package com.example.travelhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(
	    exclude = {
	            DataSourceAutoConfiguration.class,
	            HibernateJpaAutoConfiguration.class,
	                    MongoAutoConfiguration.class,
	                    MongoDataAutoConfiguration.class
	                
	        }
	    )
public class TravelHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelHubApplication.class, args);
    }
}
