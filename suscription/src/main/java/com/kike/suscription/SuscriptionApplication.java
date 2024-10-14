package com.kike.suscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SuscriptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuscriptionApplication.class, args);
	}

}
