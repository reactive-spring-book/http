package rsb.security.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceApplication {

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "service");// <1>
		SpringApplication.run(ServiceApplication.class, args);
	}

}
