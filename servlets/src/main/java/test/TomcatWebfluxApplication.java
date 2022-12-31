package test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class TomcatWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(TomcatWebfluxApplication.class, args);
	}

}

record Greetings(String message) {

	static Mono<Greetings> greet(String contextPath, String name) {
		return Mono.just(new Greetings("Hello, " + name + " from " + contextPath + "!"));
	}
}

@Configuration
class GreetingsRouteConfiguration {

	@Bean
	RouterFunction<ServerResponse> routes() {
		return route()//
				.GET("/hello/functional/{name}", request -> {
					var reply = Greetings.greet("functional", request.pathVariable("name"));
					return ok().contentType(MediaType.APPLICATION_JSON).body(reply, Greetings.class);
				}) //
				.build();
	}

}

@Slf4j
@RestController
class GreetingsController {

	@GetMapping(value = "/hello/controller/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	Mono<Greetings> greet(ServerHttpRequest request, @PathVariable String name) {
		return Greetings.greet("controller", name);
	}

}