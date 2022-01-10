package rsb.http.filters;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Log4j2
@Configuration
class LowercaseWebConfiguration {

	@Bean
	RouterFunction<ServerResponse> routerFunctionFilters() {
		var uuidKey = UUID.class.getName();
		return route() // <1>
				.GET("/hi/{name}", this::handle) //
				.GET("/hello/{name}", this::handle) //
				.filter((req, next) -> {// <2>
					log.info(".filter(): before");
					var reply = next.handle(req);
					log.info(".filter(): after");
					return reply;
				}) //
				.before(request -> {
					log.info(".before()"); // <3>
					request.attributes().put(uuidKey, UUID.randomUUID());
					return request;
				}) //
				.after((serverRequest, serverResponse) -> {
					log.info(".after()"); // <4>
					log.info("UUID: " + serverRequest.attributes().get(uuidKey));
					return serverResponse;
				}) //
				.onError(NullPointerException.class, (e, request) -> badRequest().build()) // <5>
				.build();
	}

	Mono<ServerResponse> handle(ServerRequest serverRequest) {
		return ok().bodyValue(String.format("Hello, %s!", serverRequest.pathVariable("name")));
	}

}
