package rsb.http.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
class SimpleFunctionalEndpointConfiguration {

	@Bean
	RouterFunction<ServerResponse> simple(GreetingsHandlerFunction handler) { // <1>

		// <2>
		return route() //
				.GET("/hello/{name}", request -> { // <3>
					var namePathVariable = request.pathVariable("name");
					var message = String.format("Hello %s!", namePathVariable);
					return ok().syncBody(message);
				}) //
				.GET("/hodor", handler) // <4>
				.GET("/sup", handler::handle) // <5>
				.build();
	}

}
