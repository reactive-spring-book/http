package rsb.http.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
class SimpleFunctionalEndpointConfiguration {

	@Bean
	RouterFunction<ServerResponse> greetingsRoutes() {

		var handlerFunction = new GreetingsHandlerFunction();

		return route(GET("/hodor"),
				request -> ok().body(Mono.just("Hodor!"), String.class)) // <1>
						.andRoute(GET("/yo"), handlerFunction) // <2>
						.andRoute(GET("/sup"), handlerFunction::handle); // <3>
	}

	private static class GreetingsHandlerFunction
			implements HandlerFunction<ServerResponse> {

		@Override
		public Mono<ServerResponse> handle(ServerRequest request) {
			return ok().syncBody("Hi!");
		}

	}

}
