package rsb.client.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
class TimingWebClientCustomizer implements WebClientCustomizer {

	@Override
	public void customize(WebClient.Builder webClientBuilder) {
		webClientBuilder.filter(new TimingExchangeFilterFunction()); // <1>
	}

}
