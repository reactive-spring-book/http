package rsb.http.filters;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

class LowercaseWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange incomingExchange, WebFilterChain chain) {

		// <1>
		var lowercaseUri = URI
				.create(incomingExchange.getRequest().getURI().toString().toLowerCase());

		var outgoingExchange = incomingExchange.mutate() // <2>
				.request(builder -> builder.uri(lowercaseUri)).build();

		return chain.filter(outgoingExchange); // <3>
	}

}
