package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
public class TomcatWebfluxApplicationTest {

	@Autowired
	private WebTestClient client;

	@Test
	public void greet() throws Exception {

		this.client//
				.get()//
				.uri("/hello")//
				.exchange()//
				.expectStatus()//
				.isOk()//
				.expectHeader().contentType(MediaType.TEXT_PLAIN_VALUE)//
				.expectBody(String.class).value(returnedValue -> Assertions.assertEquals(returnedValue, "Hi!"));
	}

}