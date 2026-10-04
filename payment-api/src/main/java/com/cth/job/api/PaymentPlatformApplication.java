package com.cth.job.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.cth.job")
@EntityScan(basePackages = "com.cth.job")
@EnableJpaRepositories(basePackages = "com.cth.job")
public class PaymentPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentPlatformApplication.class, args);
    }
}
