package ws;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import java.net.URI;

@Log4j2
@SpringBootTest(webEnvironment =
	SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class WsApplicationTest {

	private final WebSocketClient socketClient = new ReactorNettyWebSocketClient();

	@Test
	public void testNotificationsOnUpdates() throws Exception {

		URI uri = URI.create("ws://localhost:8080/ws/messages");

		// this will demonstrate the request/reply for the messages.
		socketClient
			.execute(uri, session ->

				session.send(
				session
					.receive()
					.map(WebSocketMessage::getPayloadAsText)
					.map(str -> str + " reply")
					.map(session::textMessage)
				)
					.then()
			)
			.subscribe();

		Thread.sleep(10 * 1000);

	}
}