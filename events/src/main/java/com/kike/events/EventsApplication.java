package com.kike.events;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Events microservice REST API Documentation",
				description = "FastBook Events microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Enrique Garcia",
						email = "kike@gmail.com",
						url = "https://kikegm.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://kikegm.com"
				)
		)
)
public class EventsApplication {
	public static void main(String[] args) {
		SpringApplication.run(EventsApplication.class, args);
	}

}
