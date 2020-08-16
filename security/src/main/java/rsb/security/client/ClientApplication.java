package rsb.security.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Log4j2
@SpringBootApplication
public class ClientApplication {

	public static void main(String args[]) {
		SpringApplication.run(ClientApplication.class, args);
	}

	private final String username = "jlong";

	private final String password = "pw";

	@Bean
	WebClient webClient(WebClient.Builder builder) {
		return builder.filter(
				ExchangeFilterFunctions.basicAuthentication(this.username, this.password))
				.build();
	}

	@Bean
	ApplicationListener<ApplicationReadyEvent> client(WebClient secureHttpClient) {
		return event ->

		{
			ParameterizedTypeReference<Map<String, String>> mapParameterizedTypeReference = new ParameterizedTypeReference<>() {
			};
			secureHttpClient.get().uri("http://localhost:8080/greetings").retrieve()
					.bodyToMono(mapParameterizedTypeReference)
					.subscribe(map -> log.info("greeting: " + map.get("greetings")));
		};

	}

}
