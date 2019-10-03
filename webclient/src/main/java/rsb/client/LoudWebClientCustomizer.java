package rsb.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.*;
import org.springframework.web.reactive.function.client.support.ClientResponseWrapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Log4j2
@SuppressWarnings("unused")
@Component
class LoudWebClientCustomizer implements WebClientCustomizer {

	static class LoggingClientResponseWrapper extends ClientResponseWrapper {

		LoggingClientResponseWrapper(ClientResponse delegate) {
			super(delegate);
		}

		private void start() {
			log.info("start @ " + Instant.now().toString());
		}

		private void stop() {
			log.info("stop @ " + Instant.now().toString());
		}

		private <T> Mono<T> log(Mono<T> c) {
			return c.doOnSubscribe(s -> start()).doFinally(s -> stop());
		}

		private <T> Flux<T> log(Flux<T> c) {
			return c.doOnSubscribe(s -> start()).doFinally(s -> stop());
		}

		// TODO can this be made simpler?
		@Override
		public <T> T body(BodyExtractor<T, ? super ClientHttpResponse> extractor) {
			T body = super.body(extractor);
			if (body instanceof Flux) {
				return (T) log((Flux) body);
			}
			if (body instanceof Mono) {
				return (T) log((Mono) body);
			}
			return body;
		}

		@Override
		public <T> Mono<T> bodyToMono(Class<? extends T> elementClass) {
			return log(super.bodyToMono(elementClass));
		}

		@Override
		public <T> Mono<T> bodyToMono(ParameterizedTypeReference<T> elementTypeRef) {
			return log(super.bodyToMono(elementTypeRef));
		}

		@Override
		public <T> Flux<T> bodyToFlux(Class<? extends T> elementClass) {
			return log(super.bodyToFlux(elementClass));
		}

		@Override
		public <T> Flux<T> bodyToFlux(ParameterizedTypeReference<T> elementTypeRef) {
			return log(super.bodyToFlux(elementTypeRef));
		}

	}

	static class TimingExchangeFilterFunction implements ExchangeFilterFunction {

		@Override
		public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
			Mono<ClientResponse> exchange = next.exchange(request);
			return exchange.map(LoggingClientResponseWrapper::new);
		}

	}

	@Override
	public void customize(WebClient.Builder webClientBuilder) {
		log.info("customizing the " + WebClient.Builder.class.getName() + ".");
		webClientBuilder.filter(new TimingExchangeFilterFunction());
	}

}
