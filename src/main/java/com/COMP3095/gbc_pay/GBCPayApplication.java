package com.COMP3095.gbc_pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GBCPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GBCPayApplication.class, args);
    }
}
