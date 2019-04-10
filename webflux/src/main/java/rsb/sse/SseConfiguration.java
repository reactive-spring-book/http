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

	@Bean
	RouterFunction<ServerResponse> routes() {
		return route() //
				.GET("/sse/{count}", this::handleSse) //
				.build();
	}

	Runnable log(String msg, String tag) {
		return () -> log.info(msg + " " + tag);
	}

	String CANCEL = "cancel";

	String TERMINATE = "terminate";

	String SUBSCRIBE = "subscribe";

	String COMPLETE = "complete";

	/*
	 * <T> Mono<T> from(Mono<T> in, String tag) { in.doOnCancel(this.log(CANCEL, tag));
	 * in.doOnTerminate(this.log(TERMINATE, tag)); in.doOnSubscribe(sub ->
	 * log.info(SUBSCRIBE + " " + tag)); in.doOnSuccess(t -> log.info("SUCCESS " + tag));
	 * return in; }
	 */

	/*
	 * <T> Flux<T> from(Flux<T> in, String tag) { in.doOnCancel(this.log(CANCEL, tag));
	 * in.doOnSubscribe(sub -> log.info(SUBSCRIBE + " " + tag)); in.doOnError(e ->
	 * log.info("error " + tag)); in.doOnComplete(this.log(COMPLETE, tag));
	 * in.doOnTerminate(this.log(TERMINATE, tag)); return in; }
	 */

	private Mono<ServerResponse> handleSse(ServerRequest r) {
		var countPathVariable = Integer.parseInt(r.pathVariable("count"));
		var publisher = IntervalMessageProducer.produce(countPathVariable)
				.doOnComplete(() -> log.info("completed"));

		return ServerResponse //
				.ok() //
				.contentType(MediaType.TEXT_EVENT_STREAM) // <1>
				.body(publisher, String.class);

	}

}
