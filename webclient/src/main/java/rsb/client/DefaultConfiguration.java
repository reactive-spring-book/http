package rsb.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@Log4j2
@Configuration
public class DefaultConfiguration {

	@Bean
	DefaultClient defaultClient(WebClient.Builder builder, ClientProperties properties) {
		var root = properties.getHttp().getRootUrl();
		return new DefaultClient(builder.baseUrl(root).build());// <1>
	}

}
