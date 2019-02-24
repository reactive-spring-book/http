package sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Log4j2
@RestController
class SseController {

	@GetMapping(path = "/sse/{count}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Flux<String> sse(@PathVariable int count) {

		@AllArgsConstructor
		@Data
		class CountAndString {

			private String message;

			private long count;

		}

		var counter = new AtomicLong();

		var generate = Stream.generate(() -> {
			var nextValue = counter.incrementAndGet();
			return new CountAndString("# " + nextValue, nextValue);
		});

		return Flux //
				.fromStream(generate).takeWhile(s -> s.getCount() <= count)
				.map(CountAndString::getMessage).delayElements(Duration.ofSeconds(1));

	}

}