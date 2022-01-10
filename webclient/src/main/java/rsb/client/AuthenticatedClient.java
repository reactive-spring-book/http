package rsb.client;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public record AuthenticatedClient(WebClient client) {

	public Mono<Greeting> getAuthenticatedGreeting() {
		return this.client//
				.get()// <1>
				.uri("/greet/authenticated")//
				.retrieve()//
				.bodyToMono(Greeting.class);//
	}

}
