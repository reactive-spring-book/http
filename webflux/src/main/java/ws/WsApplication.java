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

@Log4j2
@SpringBootApplication
public class WsApplication {

	@Bean
	WebSocketHandlerAdapter webSocketHandlerAdapter() {
		return new WebSocketHandlerAdapter();
	}

	private static void log(String m) {
		System.out.println(m);
	}

	@Bean
	WebSocketHandler webSocketHandler() {
		return session -> {
			var out = IntervalMessageProducer.produce().doOnNext(WsApplication::log)
					.map(session::textMessage).doFinally(signalType -> {
						if (signalType.equals(SignalType.CANCEL)) {
							log("somebody disconnected!");
						}
						else {
							log("signal type: " + signalType);
						}
					});
			return session //
					.send(out) //
					.and(session.receive() //
							.map(WebSocketMessage::getPayloadAsText) //
							.doOnNext(WsApplication::log) //
			);
		};
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
