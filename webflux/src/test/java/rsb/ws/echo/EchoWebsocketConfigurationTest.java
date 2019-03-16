package rsb.ws.echo;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.test.StepVerifier;
import rsb.ws.WsApplication;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		WsApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EchoWebsocketConfigurationTest {

	@Test
	public void testNotificationsOnUpdates() throws Exception {
		var socketClient = new ReactorNettyWebSocketClient();
		int max = 2;
		var values = new ArrayList<>();
		var uri = URI.create("ws://localhost:8080/ws/messages");
		var execute = socketClient.execute(uri, session -> {
			var map = session.receive() //
					.map(WebSocketMessage::getPayloadAsText) //
					.map(str -> str + " reply") //
					.doOnNext(values::add) //
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