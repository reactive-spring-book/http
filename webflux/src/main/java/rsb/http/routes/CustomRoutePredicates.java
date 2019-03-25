package rsb.http.routes;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.function.Supplier;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static rsb.http.routes.CaseInsensitiveRequestPredicate.i;

@Log4j2
@Configuration
class CustomRoutePredicates {

	@Bean
	RouterFunction<ServerResponse> customRequestPredicates() {

		// <1>
		Supplier<RouterFunction<ServerResponse>> handler = () -> r -> Mono
				.just(CustomRoutePredicates::handle);

		// <2>
		var aPeculiarRequestPredicate = GET("/test") //
				.and(accept(MediaType.APPLICATION_JSON_UTF8)) //
				.and(CustomRoutePredicates::isRequestForAValidUid);

		// <3>
		var caseInsensitiveRequestPredicate = i(GET("/greetings/{name}"));

		// <4>
		return route().nest(aPeculiarRequestPredicate, handler)
				.nest(caseInsensitiveRequestPredicate, handler).build();
	}

	private static Mono<ServerResponse> handle(ServerRequest r) {
		return ok().syncBody("Hello, " + r.queryParam("name").orElse("world") + "!");
	}

	private static boolean isRequestForAValidUid(ServerRequest request) {
		var goodUids = Set.of("1", "2", "3");
		return request.queryParam("uid").map(goodUids::contains).orElse(false);
	}

}
