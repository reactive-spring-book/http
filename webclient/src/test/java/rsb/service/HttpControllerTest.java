package rsb.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import rsb.client.*;

@SpringBootTest(classes = { DefaultConfiguration.class, ClientProperties.class,
		HttpServiceApplication.class }, //
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, //
		properties = { "spring.profiles.active=client", "server.port=8080",
				"spring.main.web-application-type=reactive" })
@RunWith(SpringRunner.class)
public class HttpControllerTest {

	// @Autowired
	// private AuthenticatedClient authenticatedClient;

	@Autowired
	private DefaultClient defaultClient;

	@Test
	public void greetSingle() {
		Mono<Greeting> helloMono = this.defaultClient.getSingle("Madhura");
		StepVerifier.create(helloMono)
				.expectNextMatches(g -> g.getMessage().contains("Hello Madhura"))
				.verifyComplete();
	}

	@Test
	public void greetMany() {
		Flux<Greeting> helloFlux = this.defaultClient.getMany("Stephane").take(2);
		String msg = "Hello Stephane";
		StepVerifier.create(helloFlux)
				.expectNextMatches(g -> g.getMessage().contains(msg))
				.expectNextMatches(g -> g.getMessage().contains(msg)).verifyComplete();
	}

	/*
	 * @Test public void greetAuthenticated() { Mono<Greeting> authenticatedGreeting =
	 * this.authenticatedClient.getAuthenticatedGreeting();
	 * StepVerifier.create(authenticatedGreeting).expectNextMatches(g ->
	 * g.getMessage().contains("Hello jlong")).verifyComplete(); }
	 */

}