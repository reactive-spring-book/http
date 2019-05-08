package rsb.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@SpringBootApplication
public class HttpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpServiceApplication.class, args);
	}

}

@RestController
class HttpController {

	@GetMapping(value = "/greet/single/{name}")
	Publisher<Greeting> greetSingle(@PathVariable String name) {
		return Mono.just(greeting(name));
	}

	@GetMapping(value = "/greet/many/{name}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Publisher<Greeting> greetMany(@PathVariable String name) {
		return Flux.fromStream(Stream.generate(() -> greeting(name)))
				.delayElements(Duration.ofSeconds(1));
	}

	Greeting greeting(String name) {
		return new Greeting("Hello " + name + " @ " + Instant.now());
	}

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Greeting {

	private String message;

}