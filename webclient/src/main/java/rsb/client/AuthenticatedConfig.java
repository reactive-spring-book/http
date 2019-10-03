package rsb.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Log4j2
@Configuration
class AuthenticatedConfig {

	private final WebClient client;

	AuthenticatedConfig(WebClient.Builder builder, ClientProperties clientProperties) {
		var httpProperties = clientProperties.getHttp();
		var basicAuthProperties = clientProperties.getHttp().getBasic();
		var filterFunction = ExchangeFilterFunctions.basicAuthentication(
				basicAuthProperties.getUsername(), basicAuthProperties.getPassword());
		this.client = builder//
				.baseUrl(httpProperties.getRootUrl())//
				.filters(filters -> filters.add(filterFunction)) //
				.build();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void readyForAuthenticatedRequest() {
		this.client.get()//
				.uri("/greet/authenticated")//
				.retrieve()//
				.bodyToMono(Greeting.class)//
				.subscribe(log::info);
	}

}
