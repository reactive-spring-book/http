package rsb.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Log4j2
@Configuration
class DefaultConfig {

	private final WebClient client;

	DefaultConfig(WebClient.Builder builder, ClientProperties properties) {
		var root = properties.getHttp().getRootUrl();
		this.client = builder.baseUrl(root).build();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void go() {

		client.get()//
				.uri("/greet/single/{name}", Map.of("name", "Josh"))//
				.retrieve()//
				.bodyToMono(Greeting.class)//
				.subscribe(log::info);

		client.get()//
				.uri("/greet/many/{name}", Map.of("name", "Tammie"))//
				.retrieve()//
				.bodyToFlux(Greeting.class)//
				.subscribe(log::info);
	}

}
