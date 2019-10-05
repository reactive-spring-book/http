package rsb.client.timer;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j2
@Component
class TimingWebClientCustomizer implements WebClientCustomizer {

	@Override
	public void customize(WebClient.Builder webClientBuilder) {
		webClientBuilder.filter(new TimingExchangeFilterFunction()); // <1>
	}

}
