package rsb.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class DefaultClient {

	private final WebClient client;

	public Mono<Greeting> getSingle(String name) {
		// <1>
		return client.get()//
				.uri("/greet/single/{name}", Map.of("name", name))// "Stéphane Maldini"
				.retrieve()//
				.bodyToMono(Greeting.class);
	}

	public Flux<Greeting> getMany(String name) {
		// <2>
		return client.get()//
				.uri("/greet/many/{name}", Map.of("name", name))// "Madhura Bhave"
				.retrieve()//
				.bodyToFlux(Greeting.class)//
				.take(10);
	}

}
