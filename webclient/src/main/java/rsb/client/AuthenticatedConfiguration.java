package rsb.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j2
@Configuration
public class AuthenticatedConfiguration {

	@Bean
	AuthenticatedClient authenticatedClient(WebClient.Builder builder,
			ClientProperties clientProperties) {
		// <1>
		var httpProperties = clientProperties.getHttp();
		var basicAuthProperties = clientProperties.getHttp().getBasic();
		// <2>
		var filterFunction = ExchangeFilterFunctions.basicAuthentication(
				basicAuthProperties.getUsername(), basicAuthProperties.getPassword());
		// <3>
		WebClient client = builder//
				.baseUrl(httpProperties.getRootUrl())//
				.filters(filters -> filters.add(filterFunction)) //
				.build();
		return new AuthenticatedClient(client);
	}

}
