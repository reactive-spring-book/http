package rsb.ws.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
class ChatWebsocketConfiguration {

	// <1>
	ChatWebsocketConfiguration(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	// <2>
	private final Map<String, Connection> sessions = new ConcurrentHashMap<>();

	// <3>
	private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

	private final ObjectMapper objectMapper;

	@Bean
	WebSocketHandler chatWsh() {

		// <1>
		var executor = Executors.newSingleThreadExecutor();
		var messagesToBroadcast = Flux.<Message>create(sink -> {
			var submit = executor.submit(() -> {
				while (true) {
					try {
						sink.next(this.messages.take());
					}
					catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			});
			sink.onCancel(() -> submit.cancel(true));
		}) //
				.share();

		return session -> {

			var sessionId = session.getId();

			this.sessions.put(sessionId, new Connection(sessionId, session));

			var in = session //
					.receive() //
					.map(WebSocketMessage::getPayloadAsText) //
					.map(this::messageFromJson) //
					.map(msg -> new Message(sessionId, msg.getText(), new Date())) //
					.map(x -> {
						var result = this.messages.offer(x);
						return result;
					})//
					.doFinally(st -> { //
						if (st.equals(SignalType.ON_COMPLETE)) {//
							this.sessions.remove(sessionId);//
						}
					}); //

			var out = messagesToBroadcast //
					.map(this::jsonFromMessage)//
					.map(session::textMessage);

			return session.send(out).and(in);
		};
	}

	@Bean
	HandlerMapping chatHm() {
		return new SimpleUrlHandlerMapping() {
			{
				this.setUrlMap(Map.of("/ws/chat", chatWsh()));
				this.setOrder(2);
			}
		};
	}

	@SneakyThrows
	private Message messageFromJson(String json) {
		return this.objectMapper.readValue(json, Message.class);
	}

	@SneakyThrows
	private String jsonFromMessage(Message msg) {
		return this.objectMapper.writeValueAsString(msg);
	}

}
