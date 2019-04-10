package rsb.ws.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import rsb.HttpApplication;
import rsb.ws.WebsocketConfiguration;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { HttpApplication.class, ChatWebsocketConfiguration.class,
		WebsocketConfiguration.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatWebsocketConfigurationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private ObjectMapper objectMapper;

	@SneakyThrows
	private String from(Message msg) {
		return objectMapper.writeValueAsString(msg);
	}

	@SneakyThrows
	private Message from(String json) {
		return objectMapper.readValue(json, Message.class);
	}

	@Test
	public void chat() {

		var message = new Message(null, "Hello, world!", null);
		var uri = URI.create("ws://localhost:" + this.port + "/ws/chat");
		var lists = new ArrayList<Message>();

		var out = new ReactorNettyWebSocketClient() //
				.execute(uri, session -> {

					var send = Flux //
							.just(message) //
							.map(this::from) //
							.map(session::textMessage);

					return session.send(send);
				});

		var in = new ReactorNettyWebSocketClient()//
				.execute(uri, session -> session //
						.receive() //
						.map(WebSocketMessage::getPayloadAsText) //
						.map(this::from) //
						.doOnNext(lists::add) //
						.take(1)//
						.then());

		StepVerifier //
				.create(in.and(out)) //
				.expectComplete() //
				.verify(Duration.ofSeconds(20));

		Assert.assertEquals(lists.size(), 1);
		var next = lists.iterator().next();
		Assert.assertNotNull(next.getWhen());
		Assert.assertNotNull(next.getClientId());
		Assert.assertEquals(next.getText(), message.getText());

	}

}