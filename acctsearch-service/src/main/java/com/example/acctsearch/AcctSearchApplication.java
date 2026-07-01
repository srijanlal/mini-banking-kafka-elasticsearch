package com.example.acctsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AcctSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcctSearchApplication.class, args);
    }
}
