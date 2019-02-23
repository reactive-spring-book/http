package http;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.net.URI;

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
		RouterFunction<ServerResponse> customerRoutes =
				// route(method(HttpMethod.GET), handler::handleFindAll)
				// .andRoute(method(HttpMethod.POST), handler::handleCreateCustomer)
				route(RequestPredicates.GET(prefix + "/{id}"),
						handler::handleFindCustomerById);

		return RouterFunctions
				.route(RequestPredicates.GET(prefix), handler::handleFindAll)
				.andRoute(RequestPredicates.GET(prefix + "/{id}"),
						handler::handleFindCustomerById)
				.andRoute(RequestPredicates.POST(prefix), handler::handleCreateCustomer);
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
