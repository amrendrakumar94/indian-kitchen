package com.example.kitchen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableCaching
@EnableRetry
@EnableScheduling
@ConfigurationPropertiesScan
public class KitchenApplication {

	public static void main(String[] args) {
		SpringApplication.run(KitchenApplication.class, args);
	}

}
