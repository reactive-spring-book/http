package rsb.security.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

	// <1>
	@Bean
	WebClient webClient(WebClient.Builder builder) {
		var username = "jlong";
		var password = "pw";
		var basicAuthentication = ExchangeFilterFunctions.basicAuthentication(username, password);
		return builder//
				.filter(basicAuthentication)// <2>
				.build();//
	}

	// <2>
	@Bean
	ApplicationListener<ApplicationReadyEvent> client(WebClient secureHttpClient) {
		return event -> secureHttpClient//
				.get()//
				.uri("http://localhost:8080/greetings")//
				.retrieve()//
				.bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
				})// <3>
				.subscribe(map -> log.info("greeting: " + map.get("greetings")));

	}

}
