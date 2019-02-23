package http;

import org.reactivestreams.Publisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/rc/customers")
class CustomerRestController {

	private final CustomerRepository customerRepository;

	CustomerRestController(CustomerRepository cr) {
		this.customerRepository = cr;
	}

	@GetMapping("/{id}")
	Mono<Customer> byId(@PathVariable String id) {
		return this.customerRepository.findById(id);
	}

	@GetMapping
	Flux<Customer> all() {
		return this.customerRepository.findAll();
	}

	@PostMapping
	Mono<ResponseEntity<?>> create(@RequestBody Customer customer) {
		return this.customerRepository.save(customer) //
				.map(body -> ResponseEntity //
						.created(URI.create("/rc/customers/" + body.getId())) //
						.build());
	}

}
