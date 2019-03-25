package rsb.http.routes;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
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

@Log4j2
@Configuration
class NestedFunctionalEndpointConfiguration {

	private HandlerFunction<ServerResponse> pathVariableHandlerFunction = request -> ok()
			.syncBody(greet(Optional.of(request.pathVariable("pv"))));

	private HandlerFunction<ServerResponse> noPathVariableHandlerFunction = request -> ok()
			.syncBody(greet(Optional.ofNullable(null)));

	private HandlerFunction<ServerResponse> sseHandlerFunction = request -> ok() //
			.contentType(TEXT_EVENT_STREAM) //
			.body(IntervalMessageProducer.produce(), String.class);

	@Bean
	RouterFunction<ServerResponse> nested() {
		var jsonRequestPredicate = accept(APPLICATION_JSON)
				.or(accept(APPLICATION_JSON_UTF8));
		var sseRequestPredicate = accept(TEXT_EVENT_STREAM);
		return route() //
				.nest(path("/nested"), builder -> builder
						.nest(jsonRequestPredicate,
								nestedBuilder -> nestedBuilder
										.GET("", this.noPathVariableHandlerFunction)
										.GET("/{pv}", this.pathVariableHandlerFunction))
						.nest(sseRequestPredicate, nestedBuilder -> nestedBuilder.GET("",
								this.sseHandlerFunction)))
				.build();
	}

	private static Map<String, String> greet(Optional<String> name) {
		var fmt = "Hello %s!";
		var message = name.map(realizedName -> String.format(fmt, realizedName)) //
				.orElse(String.format(fmt, "world"));
		return Map.of("message", message);
	}

}
