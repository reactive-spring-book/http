package rsb.http.customers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController // <1>
@RequestMapping(value = "/rc/customers") // <2>
class CustomerRestController {

	private final CustomerRepository customerRepository;

	CustomerRestController(CustomerRepository cr) {
		this.customerRepository = cr;
	}

	@GetMapping("/{id}") // <3>
	Mono<Customer> byId(@PathVariable("id") String id) {
		return this.customerRepository.findById(id);
	}

	@GetMapping // <4>
	Flux<Customer> all() {
		return this.customerRepository.findAll();
	}

	@PostMapping // <5>
	Mono<ResponseEntity<?>> create(@RequestBody Customer customer) { // <6>
		return this.customerRepository.save(customer) //
				.map(body -> ResponseEntity //
						.created(URI.create("/rc/customers/" + body.getId())) //
						.build());
	}

}
