package rsb.http.filters;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Log4j2
@Configuration
class LowercaseWebConfiguration {

	// for testing
	static String FMT = "Hello, %s!";

	Mono<ServerResponse> handle(ServerRequest serverRequest) {
		return ok().syncBody(String.format(FMT, serverRequest.pathVariable("name")));
	}

	@Bean
	RouterFunction<ServerResponse> routes() {

		var before = new Function<ServerRequest, ServerRequest>() {

			@Override
			public ServerRequest apply(ServerRequest serverRequest) {
				log.info("request is arriving @ " + Instant.now());
				return serverRequest;
			}
		};

		var after = new BiFunction<ServerRequest, ServerResponse, ServerResponse>() {

			@Override
			public ServerResponse apply(ServerRequest serverRequest,
					ServerResponse serverResponse) {
				log.info("request is leaving @ " + Instant.now());
				return serverResponse;
			}
		};

		return route().before(before).GET("/hi/{name}", this::handle)
				.GET("/hello/{name}", this::handle).after(after).build();
	}

	@Bean
	LowercaseWebFilter lowercaseWebFilter() {
		return new LowercaseWebFilter();
	}

}
