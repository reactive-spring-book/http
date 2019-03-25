package rsb.http.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import rsb.utils.IntervalMessageProducer;

import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
class NestedFunctionalEndpointConfiguration {

	// <1>
	private MediaType sseMT = TEXT_EVENT_STREAM;

	private MediaType jsonUtf8MT = APPLICATION_JSON_UTF8;

	private MediaType jsonMT = APPLICATION_JSON;

	// <2>
	private HandlerFunction<ServerResponse> pathVariableHF = //
			request -> ok().syncBody(greet(Optional.of(request.pathVariable("pv"))));

	private HandlerFunction<ServerResponse> noPathVariableHF = //
			request -> ok().syncBody(greet(Optional.ofNullable(null)));

	// <3>
	private HandlerFunction<ServerResponse> sseHF = //
			request -> ok() //
					.contentType(sseMT) //
					.body(IntervalMessageProducer.produce(), String.class);

	@Bean
	RouterFunction<ServerResponse> nested() {

		// <4>
		var jsonRP = accept(jsonMT).or(accept(jsonUtf8MT));
		var sseRP = accept(sseMT);

		// <5>
		return route() //
				.nest(path("/nested"), builder -> builder //
						.nest(jsonRP, nestedBuilder -> nestedBuilder //
								.GET("", this.noPathVariableHF) //
								.GET("/{pv}", this.pathVariableHF) //
						) //
						.nest(sseRP, nestedBuilder -> nestedBuilder.GET("", this.sseHF)))
				.build();
	}

	private static Map<String, String> greet(Optional<String> name) {
		var fmt = "Hello %s!";
		var message = name //
				.map(realizedName -> String.format(fmt, realizedName)) //
				.orElse(String.format(fmt, "world"));
		return Map.of("message", message);
	}

}
