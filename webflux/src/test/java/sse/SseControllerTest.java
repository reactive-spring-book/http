package sse;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@Log4j2
@WebFluxTest
@RunWith(SpringRunner.class)
public class SseControllerTest {

	@Autowired
	private WebTestClient client;

	@Test
	public void sse() {

		StepVerifier //
				.create( //
						this.client //
								.get() //
								.uri("/sse/2") //
								.exchange() //
								.expectStatus().isOk() //
								.expectHeader()
								.contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM) //
								.returnResult(String.class) //
								.getResponseBody()//
				) //
				.expectNext("# 1") //
				.expectNext("# 2") //
				.verifyComplete();

	}

}