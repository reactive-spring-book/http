package rsb.service;

import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rsb.client.Greeting;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@SpringBootApplication
public class HttpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpServiceApplication.class, args);
	}

	@Bean
	SecurityWebFilterChain authorization(ServerHttpSecurity http) {
		http.httpBasic();
		http.csrf().disable();
		http.authorizeExchange().pathMatchers("/greet/authenticated").authenticated()
				.anyExchange().permitAll();
		return http.build();
	}

	@Bean
	MapReactiveUserDetailsService authentication() {
		var jlong = User.withDefaultPasswordEncoder().username("jlong").roles("USER")
				.password("pw").build();
		return new MapReactiveUserDetailsService(jlong);
	}

}

@RestController
class HttpController {

	@GetMapping("/greet/authenticated")
	Publisher<Greeting> greetAuthenticated(Authentication authentication) {
		return Mono.just(greeting(authentication.getName()));
	}

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
