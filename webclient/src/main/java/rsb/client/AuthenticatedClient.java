package rsb.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
public class AuthenticatedClient {

	private final WebClient client;

	public Mono<Greeting> getAuthenticatedGreeting() {
		return this.client//
				.get()// <1>
				.uri("/greet/authenticated")//
				.retrieve()//
				.bodyToMono(Greeting.class);//
	}

}
