package rsb.http.routes;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Set;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static rsb.http.routes.CaseInsensitiveRequestPredicate.i;

@Log4j2
@Configuration
class CustomRoutePredicates {

	private final HandlerFunction<ServerResponse> handler = //
			request -> ok().syncBody(
					"Hello, " + request.queryParam("name").orElse("world") + "!");

	@Bean
	RouterFunction<ServerResponse> customRequestPredicates() {

		var aPeculiarRequestPredicate = GET("/test") // <1>
				.and(accept(MediaType.APPLICATION_JSON_UTF8)) //
				.and(this::isRequestForAValidUid);

		var caseInsensitiveRequestPredicate = i(GET("/greetings/{name}")); // <2>

		return route() //
				.add(route(aPeculiarRequestPredicate, this.handler)) //
				.add(route(caseInsensitiveRequestPredicate, this.handler)) //
				.build();
	}

	boolean isRequestForAValidUid(ServerRequest request) {
		var goodUids = Set.of("1", "2", "3");
		return request //
				.queryParam("uid") //
				.map(goodUids::contains) //
				.orElse(false);
	}

}
