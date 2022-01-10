package rsb.http.customers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController // <1>
@RequestMapping(value = "/rc/customers") // <2>
record CustomerRestController(CustomerRepository repository) {

	@GetMapping("/{id}") // <3>
	Mono<Customer> byId(@PathVariable("id") String id) {
		return this.repository.findById(id);
	}

	@GetMapping // <4>
	Flux<Customer> all() {
		return this.repository.findAll();
	}

	@PostMapping // <5>
	Mono<ResponseEntity<?>> create(@RequestBody Customer customer) { // <6>
		return this.repository.save(customer)//
				.map(customerEntity -> ResponseEntity//
						.created(URI.create("/rc/customers/" + customerEntity.id())) //
						.build());
	}

}
