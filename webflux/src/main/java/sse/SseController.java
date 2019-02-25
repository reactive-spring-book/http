package sse;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import utils.IntervalMessageProducer;

@Log4j2
@RestController
class SseController {

	@GetMapping(path = "/sse/{count}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Flux<String> sse(@PathVariable int count) {
		return IntervalMessageProducer.produce(count);
	}

}
