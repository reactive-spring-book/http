package rsb.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
class AuthenticatedConfiguration {

	@Bean
	AuthenticatedClient authenticatedClient(WebClient.Builder builder, ClientProperties clientProperties) {
		// <1>
		var httpProperties = clientProperties.getHttp();
		var basicAuthProperties = clientProperties.getHttp().getBasic();
		// <2>
		var filterFunction = ExchangeFilterFunctions.basicAuthentication(basicAuthProperties.getUsername(),
				basicAuthProperties.getPassword());
		// <3>
		WebClient client = builder//
				.baseUrl(httpProperties.getRootUrl())//
				.filters(filters -> filters.add(filterFunction)) //
				.build();
		return new AuthenticatedClient(client);
	}

}
