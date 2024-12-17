package com.aleksandartokarev.spring_boot_websocket_artillery_load_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootWebsocketArtilleryLoadTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebsocketArtilleryLoadTestApplication.class, args);
	}

}
