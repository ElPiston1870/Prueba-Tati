package com.sistemaVeterinario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SistemaVeterinarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaVeterinarioApplication.class, args);
	}

}
