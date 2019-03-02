package http;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@WebFluxTest({ SimpleFunctionalEndpointConfiguration.class })
public class SimpleFunctionalEndpointConfigurationTest {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void sup() {
		doTest("/sup", "Hi!");
	}

	@Test
	public void hodor() {
		doTest("/hodor", "Hodor!");
	}

	@Test
	public void yo() {
		doTest("/yo", "Hi!");
	}

	private void doTest(String path, String result) {

		this.webTestClient.get().uri(path).exchange().expectBody(String.class)
				.value(str -> Assert.assertEquals(result, str));
	}

}