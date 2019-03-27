package rsb.http.routes;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@WebFluxTest({ SimpleFunctionalEndpointConfiguration.class,
		GreetingsHandlerFunction.class })
public class SimpleFunctionalEndpointConfigurationTest {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void sup() {
		this.doTest("/sup", "Hodor!");
	}

	@Test
	public void hello() {
		this.doTest("/hello/World", "Hello World!");
	}

	@Test
	public void hodor() {
		this.doTest("/hodor", "Hodor!");
	}

	private void doTest(String path, String result) {

		this.webTestClient.get() //
				.uri(path) //
				.exchange() //
				.expectBody(String.class).value(str -> Assert.assertEquals(result, str));
	}

}