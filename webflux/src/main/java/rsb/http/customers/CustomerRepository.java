package rsb.http.customers;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CustomerRepository {

	private final Map<String, Customer> data = new ConcurrentHashMap<>();

	Mono<Customer> findById(String id) {
		return Mono.just(this.data.get(id));
	}

	Mono<Customer> save(Customer customer) {
		var uuid = UUID.randomUUID().toString();
		this.data.put(uuid, new Customer(uuid, customer.name()));
		return Mono.just(this.data.get(uuid));
	}

	Flux<Customer> findAll() {
		return Flux.fromIterable(this.data.values());
	}

}
