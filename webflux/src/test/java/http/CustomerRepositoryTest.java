package http;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	public void findAll() {

		StepVerifier.create(this.customerRepository.findAll())
				.expectNextCount(customerRepository.names.size()).verifyComplete();

		StepVerifier.FirstStep<Customer> customerFirstStep = StepVerifier //
				.create(this.customerRepository.findAll());

		for (int i = 0; i < this.customerRepository.names.size(); i++)
			customerFirstStep.expectNextMatches(
					c -> this.customerRepository.names.contains(c.getName()));

		customerFirstStep.verifyComplete();
	}

	@Test
	public void findOne() {

		Mono<Customer> findRandomOne = this.customerRepository.findAll().take(1).single();

		Mono<Customer> findById = findRandomOne //
				.flatMap(c -> this.customerRepository.findOne(c.getId()));

		StepVerifier //
				.create(Flux.zip(findRandomOne, findById))
				.expectNextMatches(
						tpl -> tpl.getT1().getId().equalsIgnoreCase(tpl.getT2().getId()))
				.verifyComplete();

	}

	@Test
	public void save() {

	}

}