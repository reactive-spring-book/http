package rsb.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

@Log4j2
@Configuration
@Profile("auth")
class AuthenticatedConfig {

	private final WebClient.Builder builder;

	private final String root;

	private final String user;

	private final String pw;

	AuthenticatedConfig(WebClient.Builder builder,
			@Value("${client.http.root-url}") String root,
			@Value("${client.http.basic.username}") String usr,
			@Value("${client.http.basic.password}") String pw) {
		this.builder = builder;
		this.root = root;
		this.user = usr;
		this.pw = pw;
	}

	@Bean
	WebClient secureWebClient() {
		return this.builder.baseUrl(this.root)
				.filters(filters -> filters.add(
						ExchangeFilterFunctions.basicAuthentication(this.user, this.pw)))
				.build();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void go() {
		var client = secureWebClient();
		client.get().uri("/greet/authenticated").retrieve().bodyToMono(Greeting.class)
				.subscribe(log::info);
	}

}
