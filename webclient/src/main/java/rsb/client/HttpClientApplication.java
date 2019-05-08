package rsb.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class HttpClientApplication {

	public static void main(String[] args) {
		System.setProperty("server.port", "0");

		SpringApplication.run(HttpClientApplication.class, args);
	}

	static void accept(Greeting greeting) {
		log.info("greeting: " + greeting);
	}

}