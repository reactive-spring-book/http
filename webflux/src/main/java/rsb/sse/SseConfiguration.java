package rsb.sse;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import rsb.utils.IntervalMessageProducer;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Log4j2
@Configuration
class SseConfiguration {

	private final String countPathVariable = "count";

	@Bean
	RouterFunction<ServerResponse> routes() {
		return route() //
				.GET("/sse/{" + this.countPathVariable + "}", this::handleSse) //
				.build();
	}

	Mono<ServerResponse> handleSse(ServerRequest r) {
		var countPathVariable = Integer.parseInt(r.pathVariable(this.countPathVariable));
		var publisher = IntervalMessageProducer.produce(countPathVariable).doOnComplete(() -> log.info("completed"));

		return ServerResponse //
				.ok() //
				.contentType(MediaType.TEXT_EVENT_STREAM) // <1>
				.body(publisher, String.class);

	}

}
