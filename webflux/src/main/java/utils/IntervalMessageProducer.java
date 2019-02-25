package utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public abstract class IntervalMessageProducer {

	public static Flux<String> produce(int c) {
		return produce().take(c);
	}

	public static Flux<String> produce() {

		@AllArgsConstructor
		@Data
		class CountAndString {

			private String message;

			private long count;

		}

		var counter = new AtomicLong();

		return Flux.interval(Duration.ofSeconds(1)) //
				.map(i -> {
					var nextValue = counter.incrementAndGet();
					return new CountAndString("# " + nextValue, nextValue);
				}) //
				.map(CountAndString::getMessage);
	}

}
