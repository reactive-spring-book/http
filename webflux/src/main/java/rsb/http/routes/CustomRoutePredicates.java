package rsb.http.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static rsb.http.routes.CaseInsensitiveRequestPredicate.i;

@Configuration
class CustomRoutePredicates {

	@Bean
	RouterFunction<ServerResponse> customRequestPredicates() {

		// <1>
		var aPeculiarPredicate = GET("/test").and(accept(MediaType.APPLICATION_JSON_UTF8))
				.and(this::isRequestForAValidUid);
		var route = route(aPeculiarPredicate, this::handle);

		// <2>
		var caseInsensitiveRequestPredicate = i(GET("/greetings/{name}"));
		return route.andRoute(caseInsensitiveRequestPredicate, this::handle);

		// return route;
	}

	private Mono<ServerResponse> handle(ServerRequest r) {
		return ok().syncBody("Hello, " + r.pathVariable("name") + "!");
	}

	private boolean isRequestForAValidUid(ServerRequest request) {
		var goodUids = Set.of("1", "2", "3");
		return request.queryParam("uid").map(goodUids::contains).orElse(false);
	}

}
