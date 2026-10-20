package com.unionYHM.bootTraining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BootTrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootTrainingApplication.class, args);
	}

}
