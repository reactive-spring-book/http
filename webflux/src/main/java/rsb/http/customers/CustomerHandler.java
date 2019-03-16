package rsb.http.customers;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

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
