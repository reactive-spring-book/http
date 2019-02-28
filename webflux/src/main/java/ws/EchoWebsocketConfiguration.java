package ws;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import utils.IntervalMessageProducer;

import java.util.function.Consumer;

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
