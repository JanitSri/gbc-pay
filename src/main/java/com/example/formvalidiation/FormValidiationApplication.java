package com.example.formvalidiation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FormValidiationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormValidiationApplication.class, args);
    }

}
