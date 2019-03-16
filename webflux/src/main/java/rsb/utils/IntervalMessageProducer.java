package rsb.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

public abstract class IntervalMessageProducer {

	@Data
	@AllArgsConstructor
	private static class CountAndString {

		private String message;

		private long count;

	}

	public static Flux<String> produce(int c) {
		return produce().take(c);
	}

	public static Flux<String> produce() {

		var counter = new AtomicLong();

		return Flux.interval(Duration.ofSeconds(1)) //
				.map(i -> {
					long nextValue = counter.incrementAndGet();
					return new CountAndString("# " + nextValue, nextValue);
				}) //
				.map(CountAndString::getMessage);
	}

}
