package rsb.ws.echo;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import rsb.utils.IntervalMessageProducer;

import java.util.Map;
import java.util.function.Consumer;

@Log4j2
@Configuration
public class EchoWebsocketConfiguration {

	@Bean
	HandlerMapping echoHm() {
		return new SimpleUrlHandlerMapping() {
			{
				this.setOrder(1);
				this.setUrlMap(Map.of("/ws/messages", echoWsh()));
			}
		};
	}

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
