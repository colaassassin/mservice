package com.gateway.ssogateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SsoGateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoGateWayApplication.class, args);
    }

}
