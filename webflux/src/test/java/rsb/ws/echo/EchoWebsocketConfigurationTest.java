package rsb.ws.echo;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.test.StepVerifier;
import rsb.HttpApplication;
import rsb.ws.WebsocketConfiguration;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { HttpApplication.class,
		WebsocketConfiguration.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EchoWebsocketConfigurationTest {

	@LocalServerPort
	private int port;

	@Test
	public void testNotificationsOnUpdates() {
		var socketClient = new ReactorNettyWebSocketClient();
		var max = 2;
		var values = new ArrayList<>();
		var uri = URI.create("ws://localhost:" + this.port + "/ws/echo");
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