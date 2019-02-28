package ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import utils.IntervalMessageProducer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Log4j2
@SpringBootApplication
public class WsApplication {

	@Bean
	WebSocketHandlerAdapter webSocketHandlerAdapter() {
		return new WebSocketHandlerAdapter();
	}

	@Bean
	HandlerMapping handlerMapping(EchoWebsocketConfiguration ewc,
			ChatWebsocketConfiguration cwc) {
		return new SimpleUrlHandlerMapping() {
			{

				var endpointsToWSH = Map.of("/ws/messages", ewc.echoWsh(), "/ws/chat",
						cwc.chatWsh());
				this.setUrlMap(endpointsToWSH);
				this.setOrder(10);
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(WsApplication.class, args);
	}

}

@Log4j2
@Configuration
class ChatWebsocketConfiguration {

	@Data
	@AllArgsConstructor
	static class User {

		private String name;

		private final String id = UUID.randomUUID().toString();

	}

	/*
	 * client connects. send initial request. if the client isnt in the users map, add it.
	 * then make sure that whenever a request comes in, that we send it to all connected
	 * sessions.
	 */

	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

	@Bean
	WebSocketHandler chatWsh() {
		return session -> {
			this.sessions.put(session.getId(), session);

			return null;
		};
	}

}

@Log4j2
@Configuration
class EchoWebsocketConfiguration {

	@Bean
	WebSocketHandler echoWsh() {
		return session -> {

			Flux<WebSocketMessage> out = IntervalMessageProducer //
					.produce() //
					.doOnNext(log::info) //
					.map(session::textMessage) //
					.doFinally(consumer("outbound connection"));

			Flux<String> in = session //
					.receive() //
					.map(WebSocketMessage::getPayloadAsText) //
					.doFinally(consumer("inbound connection", st -> {
						if (st.equals(SignalType.ON_COMPLETE)) {
							session.close().subscribe();
						}
					})) //
					.doOnNext(log::info);

			return session //
					.send(out) //
					.and(in); //
		};
	}

	private static Consumer<SignalType> consumer(String msg,
			Consumer<SignalType> consumer) {
		return consumer(msg).andThen(consumer);
	}

	private static Consumer<SignalType> consumer(String msg) {
		return signalType -> log.info(msg + " : " + signalType);
	}

}
