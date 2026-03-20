package com.project.api.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableCaching // Habilita o cache
public class ECommerceApplication {
	public static void main( String[] args ) {
		SpringApplication.run( ECommerceApplication.class, args );
	}
}
