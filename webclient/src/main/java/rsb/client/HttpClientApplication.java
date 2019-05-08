package rsb.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class HttpClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpClientApplication.class, args);
	}

	@Bean
	WebClient webClient(WebClient.Builder builder) {
		return builder.build(); // todo show how to customize this
	}

}
