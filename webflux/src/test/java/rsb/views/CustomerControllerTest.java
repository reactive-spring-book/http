package rsb.views;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class CustomerControllerTest {

	private int take = 10;

	@Autowired
	private WebTestClient client;

	private Flux<String> ticker() {
		return this.client
			.get()
			.uri("/ticker.php")
			.exchange()
			.returnResult(String.class)
			.getResponseBody();
	}

	private Flux<String> stream() {
		return this.client
			.get()
			.uri("/stream.php")
			.accept(MediaType.TEXT_EVENT_STREAM)
			.exchange()
			.returnResult(String.class)
			.getResponseBody()
			.take(this.take);
	}

	Flux<String> produce() {
		return stream().doOnNext(html -> log.info("html: " + html));
	}

	@Test
	public void streamSseHtmlEndpoint() throws Exception {


		StepVerifier
			.withVirtualTime(this::produce)
			.expectNextMatches(html -> html.contains("<div") || html.contains("#"))
			.verifyComplete();

//		stream().subscribe(log::info);
		Thread.sleep(1000 * 10);


	}
}