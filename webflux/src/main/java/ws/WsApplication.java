package ws;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import ws.chat.ChatWebsocketConfiguration;
import ws.echo.EchoWebsocketConfiguration;

import java.util.Map;

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

				var endpointsToWSH = Map.of( //
						"/ws/messages", ewc.echoWsh(), //
						"/ws/chat", cwc.chatWsh() //
				);
				this.setUrlMap(endpointsToWSH);
				this.setOrder(10);
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(WsApplication.class, args);
	}

}