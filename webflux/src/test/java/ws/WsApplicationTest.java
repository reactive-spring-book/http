package ws;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class WsApplicationTest {

	private final WebSocketClient socketClient = new ReactorNettyWebSocketClient();

	@Test
	public void testNotificationsOnUpdates() throws Exception {
		int max = 2;
		List<String> values = new ArrayList<>();
		URI uri = URI.create("ws://localhost:8080/ws/messages");
		Mono<Void> execute = socketClient.execute(uri, session -> {

			Flux<WebSocketMessage> map = session.receive() //
					.map(WebSocketMessage::getPayloadAsText) //
					.map(str -> str + " reply").doOnNext(values::add) //
					.map(session::textMessage) //
					.take(max);

			return session.send(map).then();
		});
		StepVerifier //
				.create(execute) //
				.expectComplete() //
				.verify(Duration.ofSeconds(max + 2));

		Assert.assertEquals(max, values.size());
	}

}