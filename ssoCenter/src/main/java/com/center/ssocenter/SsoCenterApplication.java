package com.center.ssocenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class SsoCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoCenterApplication.class, args);
    }

}


