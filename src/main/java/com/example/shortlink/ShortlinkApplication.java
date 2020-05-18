package com.example.shortlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class ShortlinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortlinkApplication.class, args);
    }

}
