package test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class TomcatWebfluxApplication {

	@Bean
	RouterFunction<ServerResponse> routes() {
		return route()//
				.GET("/hello",
						r -> ok().contentType(MediaType.TEXT_PLAIN).bodyValue("Hi!"))
				.build();
	}

	public static void main(String args[]) {
		SpringApplication.run(TomcatWebfluxApplication.class, args);
	}

}
