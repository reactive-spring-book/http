package http;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@Configuration
@Log4j2
class CustomerEndpointConfiguration {

	@Bean
	RouterFunction<ServerResponse> routes(CustomerHandler handler) {
		String prefix = "/fn/customers";
		RouterFunction<ServerResponse> routes = //
				route(GET("/{id}"), handler::handleFindCustomerById)
						.andRoute(method(HttpMethod.GET), handler::handleFindAll)
						.andRoute(method(HttpMethod.POST), handler::handleCreateCustomer);

		return RouterFunctions.nest(path(prefix), routes);
	}

}

@Component
@Log4j2
class CustomerHandler {

	private final CustomerRepository repository;

	CustomerHandler(CustomerRepository repository) {
		this.repository = repository;
	}

	Mono<ServerResponse> handleFindAll(ServerRequest request) {
		return ok().body(this.repository.findAll(), Customer.class);
	}

	Mono<ServerResponse> handleFindCustomerById(ServerRequest request) {
		return ok().body(this.repository.findById(request.pathVariable("id")),
				Customer.class);
	}

	Mono<ServerResponse> handleCreateCustomer(ServerRequest request) {
		return request.bodyToMono(Customer.class).flatMap(repository::save)
				.flatMap(saved -> ServerResponse
						.created(URI.create("/fn/customers/" + saved.getId())).build());

	}

}
