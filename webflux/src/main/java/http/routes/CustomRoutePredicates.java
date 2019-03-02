package http.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static http.routes.CaseInsensitiveRequestPredicate.i;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
class CustomRoutePredicates {

	@Bean
	RouterFunction<ServerResponse> customRoute() {
		return route(i(GET("/greetings/{name}")),
				r -> ok().syncBody("Hello, " + r.pathVariable("name") + "!"));
	}

}
