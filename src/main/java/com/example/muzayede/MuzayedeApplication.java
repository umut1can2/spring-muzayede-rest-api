package com.example.muzayede;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MuzayedeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MuzayedeApplication.class, args);
    }

}
