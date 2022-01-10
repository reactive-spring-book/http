package rsb.client.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.support.ClientResponseWrapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
class TimingClientResponseWrapper extends ClientResponseWrapper {

	TimingClientResponseWrapper(ClientResponse delegate) {
		super(delegate);
	}

	private void start() {
		log.info("start @ " + Instant.now().toString());
	}

	private void stop() {
		log.info("stop @ " + Instant.now().toString());
	}

	// <1>
	private <T> Mono<T> log(Mono<T> c) {
		return c.doOnSubscribe(s -> start()).doFinally(s -> stop());
	}

	private <T> Flux<T> log(Flux<T> c) {
		return c.doOnSubscribe(s -> start()).doFinally(s -> stop());
	}

	// <2>
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
