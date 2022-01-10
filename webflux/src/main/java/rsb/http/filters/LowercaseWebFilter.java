package rsb.http.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
class LowercaseWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange currentRequest, WebFilterChain chain) {

		// <1>
		var lowercaseUri = URI.create(currentRequest.getRequest().getURI().toString().toLowerCase());

		var outgoingExchange = currentRequest.mutate() // <2>
				.request(builder -> builder.uri(lowercaseUri)).build();

		return chain.filter(outgoingExchange); // <3>
	}

}
