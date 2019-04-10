package rsb.http.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class NestedFunctionalEndpointConfiguration {

	@Bean
	RouterFunction<ServerResponse> nested(NestedHandler nestedHandler) {

		// <1>
		var jsonRP = accept(APPLICATION_JSON).or(accept(APPLICATION_JSON_UTF8));
		var sseRP = accept(TEXT_EVENT_STREAM);

		return route() //
				.nest(path("/nested"), builder -> builder //
						.nest(jsonRP, nestedBuilder -> nestedBuilder //
								.GET("/{pv}", nestedHandler::pathVariable) // <2>
								.GET("", nestedHandler::noPathVariable) // <3>
						).add(route(sseRP, nestedHandler::sse)) // <4>
				) //
				.build();

	}

}
