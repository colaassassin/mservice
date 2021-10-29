package com.reg.ssoreg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SsoRegApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsoRegApplication.class, args);
    }
}
