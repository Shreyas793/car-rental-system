package com.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.carrental")
@EntityScan("com.carrental.entity")
@EnableJpaRepositories("com.carrental.repository")
public class CarRentalSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarRentalSystemApplication.class, args);
    }
}
