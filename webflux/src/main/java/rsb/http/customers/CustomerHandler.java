package rsb.http.customers;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
class CustomerHandler {

	private final CustomerRepository repository;

	CustomerHandler(CustomerRepository repository) {
		this.repository = repository;
	}

	Mono<ServerResponse> handleFindAll(ServerRequest request) {
		var all = this.repository.findAll();// <1>
		return ok().body(all, Customer.class);// <2>
	}

	Mono<ServerResponse> handleFindCustomerById(ServerRequest request) {
		var id = request.pathVariable("id");
		var byId = this.repository.findById(id);
		return ok().body(byId, Customer.class);
	}

	Mono<ServerResponse> handleCreateCustomer(ServerRequest request) {
		return request.bodyToMono(Customer.class) //
				.flatMap(repository::save) //
				.flatMap(saved -> created(URI.create("/fn/customers/" + saved.id())).build());
	}

}
