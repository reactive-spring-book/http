package sse;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.time.Duration;

@Log4j2
@WebFluxTest
@RunWith(SpringRunner.class)
public class SseControllerTest {

	@Autowired
	private WebTestClient client;

	@Test
	public void sse() {

		FluxExchangeResult<String> result = this.client.get() //
				.uri("/sse/4") //
				.accept(MediaType.TEXT_EVENT_STREAM) //
				.exchange() //
				.expectStatus().isOk() //
				.expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM) //
				.returnResult(String.class);

		StepVerifier //
				.create(result.getResponseBody()) //
				.expectSubscription() //
				.thenAwait(Duration.ofSeconds(10)) //
				.expectNext("# 1") //
				.expectNext("# 2") //
				.expectNext("# 3") //
				.expectNext("# 4") //
				.thenCancel() //
				.verify();
	}

}