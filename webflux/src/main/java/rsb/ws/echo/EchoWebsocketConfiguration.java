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

@Log4j2
@Configuration
class EchoWebsocketConfiguration {

	// <1>
	@Bean
	HandlerMapping echoHm() {
		return new SimpleUrlHandlerMapping() {
			{
				this.setOrder(1);
				this.setUrlMap(Map.of("/ws/echo", echoWsh()));
			}
		};
	}

	// <2>
	@Bean
	WebSocketHandler echoWsh() {
		return session -> { // <3>

			Flux<WebSocketMessage> out = IntervalMessageProducer //
					.produce() //
					.doOnNext(log::info) //
					.map(session::textMessage) // <4>
					.doFinally(
							signalType -> log.info("outbound connection: " + signalType)); // <5>

			Flux<String> in = session //
					.receive() //
					.map(WebSocketMessage::getPayloadAsText) // <6>
					.doFinally(signalType -> {
						log.info("inbound connection: " + signalType);
						if (signalType.equals(SignalType.ON_COMPLETE)) {
							session.close().subscribe();
						}
					}).doOnNext(log::info);
			return session.send(out).and(in);// <7>
		};
	}

}
