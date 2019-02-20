package http;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	public void findAll() {

		Collection<String> names = customerRepository.names();
		StepVerifier.create(this.customerRepository.findAll())
				.expectNextCount(names.size()).verifyComplete();

		StepVerifier.FirstStep<Customer> customerFirstStep = StepVerifier //
				.create(this.customerRepository.findAll());

		for (int i = 0; i < names.size(); i++)
			customerFirstStep.expectNextMatches(c -> names.contains(c.getName()));

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

		String name = "Kimly";
		Publisher<Customer> kimly = this.customerRepository
				.save(Mono.just(new Customer(name)));

		StepVerifier //
				.create(kimly) //
				.expectNextMatches(customer -> StringUtils.hasText(customer.getId())) //
				.verifyComplete();

		StepVerifier //
				.create(this.customerRepository.findAll()
						.filter(c -> c.getName().equals(name))) //
				.expectNextCount(1).verifyComplete();

	}

}