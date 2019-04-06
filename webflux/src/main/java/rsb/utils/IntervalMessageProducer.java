package rsb.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public abstract class IntervalMessageProducer {

	@Data
	@AllArgsConstructor
	public static class CountAndString {

		private String message;

		private long count;

	}

	private static Flux<CountAndString> produceCountAndStrings() {
		var counter = new AtomicLong();

		return Flux.interval(Duration.ofSeconds(1)) // <1> 
				.map(i -> {
					long nextValue = counter.incrementAndGet();
					return new CountAndString("# " + nextValue, nextValue);
				}); //
	}

	public static Flux<String> produce(int c) {
		return produce().take(c);
	}

	public static Flux<String> produce() {
		return produceCountAndStrings().map(CountAndString::getMessage);
	}

}
