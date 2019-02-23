package http;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractRestBaseClass {

	abstract String rootUrl();

	@Autowired
	private WebTestClient client;

	@MockBean
	private CustomerRepository customerRepository;

	private final Collection<Customer> results = Arrays.asList(new Customer("1", "A"),
			new Customer("2", "B"), new Customer("3", "C"), new Customer("4", "D"));

	private final AtomicReference<Customer> saved = new AtomicReference<>();

	@Before
	public void before() throws Exception {

		Flux<Customer> iterable = Flux.fromIterable(this.results);

		Mockito //
				.when(this.customerRepository.findAll()) //
				.thenReturn(iterable);

		Mockito//
				.when(this.customerRepository.findOne("1"))//
				.thenAnswer(invocation -> Mono.just(new Customer("1", "A")));

		Mockito //
				.when(this.customerRepository.save(Mockito.any()))//
				.then(invocation -> {
					Publisher cast = Publisher.class.cast(invocation.getArguments()[0]);
					Flux<Customer> customers = Flux.from(cast);
					StepVerifier //
							.create(customers) //
							.expectNextCount(1) //
							.verifyComplete();
					saved.set(customers.blockFirst());
					String name = saved.get().getName();
					String uid = UUID.randomUUID().toString();
					return Mono.just(new Customer(uid, name));
				});
	}

	@Test
	public void all() throws Exception {

		FluxExchangeResult<Customer> customerFluxExchangeResult = this.client.get() //
				.uri(rootUrl() + "/customers") //
				.exchange() //
				.expectStatus().isOk() //
				.expectHeader() //
				.contentType(MediaType.APPLICATION_JSON_UTF8) //
				.returnResult(Customer.class);

		Flux<Customer> responseBody = customerFluxExchangeResult.getResponseBody();

		StepVerifier //
				.create(responseBody) //
				.expectNextCount(results.size()).verifyComplete();
	}

	@Test
	public void byId() {

		FluxExchangeResult<Customer> customerFluxExchangeResult = this.client.get() //
				.uri(rootUrl() + "/customers/1") //
				.exchange() //
				.expectStatus().isOk() //
				.expectHeader() //
				.contentType(MediaType.APPLICATION_JSON_UTF8) //
				.returnResult(Customer.class);

		Flux<Customer> responseBody = customerFluxExchangeResult.getResponseBody();

		StepVerifier //
				.create(responseBody) //
				.expectNextMatches(customer -> customer.getName().equalsIgnoreCase("A"))
				.verifyComplete();
	}

	@Test
	public void create() throws Exception {
		String krusty = "Krusty";
		this.client.post().uri(rootUrl() + "/customers") //
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(BodyInserters.fromObject(new Customer(krusty))).exchange()
				.expectHeader().exists(HttpHeaders.LOCATION).expectStatus().isCreated();

		Assert.assertTrue(this.saved.get().getName().equalsIgnoreCase(krusty));

	}

}
