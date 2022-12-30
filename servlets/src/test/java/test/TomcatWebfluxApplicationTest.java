package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@Import({ GreetingsRouteConfiguration.class, GreetingsController.class })
public class TomcatWebfluxApplicationTest {

	@Autowired
	private WebTestClient client;

	@Test
	void controller() {
		var name = "Kimly";
		doTest(this.client, "controller", name);
		doTest(this.client, "functional", name);
	}

	private static void doTest(WebTestClient client, String from, String name) {

		client//
				.get()//
				.uri("/hello/" + from + "/{name}", name)//
				.exchange()//
				.expectStatus()//
				.isOk()//
				.expectHeader() //
				.contentType(MediaType.APPLICATION_JSON)//
				.expectBody(Greetings.class) //
				.value(returnedValue -> Assertions.assertEquals(returnedValue.message(),
						"Hello, " + name + " from " + from + "!"));
	}

}