package test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@RunWith(SpringRunner.class)
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
				.expectBody(String.class)
				.value(returnedValue -> Assert.assertEquals(returnedValue, "Hi!"));
	}

}