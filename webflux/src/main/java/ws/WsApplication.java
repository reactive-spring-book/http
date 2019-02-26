package ws;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.SignalType;
import utils.IntervalMessageProducer;

import java.util.Collections;
import java.util.function.Consumer;

@Log4j2
@SpringBootApplication
public class WsApplication {

	@Bean
	WebSocketHandlerAdapter webSocketHandlerAdapter() {
		return new WebSocketHandlerAdapter();
	}

	@Bean
	WebSocketHandler webSocketHandler() {
		return session -> {

			var out = IntervalMessageProducer //
				.produce() //
				.doOnNext(log::info) //
				.map(session::textMessage) //
				.doFinally(consumer("outbound connection"));

			var in = session
				.receive() //
				.map(WebSocketMessage::getPayloadAsText) //
				.doFinally(consumer("inbound connection", st -> {
					if (st.equals(SignalType.ON_COMPLETE)) {
						session.close().subscribe();
					}
				}))
				.doOnNext(log::info);

			return session //
				.send(out) //
				.and(in); //
		};
	}

	private static Consumer<SignalType> consumer(
		String msg,
		Consumer<SignalType> consumer) {
		return consumer(msg).andThen(consumer);
	}

	private static Consumer<SignalType> consumer(String msg) {
		return signalType ->
			log.info(msg + " : " + signalType);
	}

	@Bean
	HandlerMapping handlerMapping() {
		return new SimpleUrlHandlerMapping() {
			{
				this.setUrlMap(
					Collections.singletonMap("/ws/messages", webSocketHandler()));
				this.setOrder(10);
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(WsApplication.class, args);
	}

}
