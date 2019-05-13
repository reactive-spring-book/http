package rsb.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Configuration
@Log4j2
@Profile("default")
class DefaultConfig {

	private final WebClient.Builder builder;

	private final String root;

	DefaultConfig(WebClient.Builder builder, @Value("${root-url}") String root) {
		this.builder = builder;
		this.root = root;
	}

	@Bean
	WebClient defaultWebClient() {
		return this.builder.baseUrl(this.root).build();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void go() {
		var client = this.defaultWebClient();
		//
		client.get().uri("/greet/single/{name}", Map.of("name", "Jane")).retrieve()
				.bodyToMono(Greeting.class).subscribe(log::info);

		//
		client.get().uri("/greet/many/{name}", Map.of("name", "Jane")).retrieve()
				.bodyToFlux(Greeting.class).subscribe(log::info);
	}

}
