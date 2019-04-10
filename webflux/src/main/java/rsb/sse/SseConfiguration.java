package rsb.sse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import rsb.utils.IntervalMessageProducer;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SseConfiguration {

	@Bean
	RouterFunction<ServerResponse> routes() {
		return route().GET("/sse/{count}", r -> ServerResponse //
				.ok() //
				.contentType(MediaType.TEXT_EVENT_STREAM) // <1>
				.body(IntervalMessageProducer
						.produce(Integer.parseInt(r.pathVariable("count"))), String.class) //
		).build();
	}

}
