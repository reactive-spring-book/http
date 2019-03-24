package rsb.http.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

class LowercaseWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange incomingExchange, WebFilterChain chain) {
		var outgoingExchange = incomingExchange.mutate()
				.request(builder -> builder.uri(URI.create(
						incomingExchange.getRequest().getURI().toString().toLowerCase())))
				.build();
		return chain.filter(outgoingExchange);
	}

}
