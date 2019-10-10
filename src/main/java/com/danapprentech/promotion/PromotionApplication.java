package com.danapprentech.promotion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class PromotionApplication {

    public static void main(String[] args) {
        SpringApplication.run (PromotionApplication.class, args);
    }

}
