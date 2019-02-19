package http;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
class CustomerRepository {

	private final Map<String, Customer> customers = new ConcurrentHashMap<>();

	// for testing
	final Collection<String> names = new ConcurrentSkipListSet<>(
			Arrays.asList("Jane", "Huiren", "Tasha", "Billy"));

	CustomerRepository() {
		this.names.stream().map(name -> new Customer(UUID.randomUUID().toString(), name))
				.forEach(c -> this.customers.put(c.getId(), c));
	}

	Flux<Customer> findAll() {
		return Flux.fromIterable(this.customers.values());
	}

	Mono<Customer> findOne(String id) {
		return Optional.ofNullable(this.customers.getOrDefault(id, null)).map(Mono::just)
				.orElse(Mono.empty());
	}

	Mono<Customer> save(Publisher<Customer> customerPublisher) {
		return Flux.from(customerPublisher).map(x -> {
			String uid = UUID.randomUUID().toString();
			Customer value = new Customer(uid, x.getName());
			this.customers.put(uid, value);
			return value;
		}).single();
	}

}
