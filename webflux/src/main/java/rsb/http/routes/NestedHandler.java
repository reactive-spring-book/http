package rsb.http.routes;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import rsb.utils.IntervalMessageProducer;

import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
class NestedHandler {

	Mono<ServerResponse> sse(ServerRequest r) {
		return ok() //
				.contentType(TEXT_EVENT_STREAM) //
				.body(IntervalMessageProducer.produce(), String.class);
	}

	Mono<ServerResponse> pathVariable(ServerRequest r) {
		return ok().syncBody(greet(Optional.of(r.pathVariable("pv"))));
	}

	Mono<ServerResponse> noPathVariable(ServerRequest r) {
		return ok().syncBody(greet(Optional.ofNullable(null)));
	}

	private Map<String, String> greet(Optional<String> name) {
		var finalName = name.orElse("world");
		return Map.of("message", String.format("Hello %s!", finalName));
	}

}
