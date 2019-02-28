package ws;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@Deprecated
@Log4j2
public class Main {

	public static void main(String args[]) throws Exception {
		streamFromQueue();
	}

	public static void streamFromQueue() throws Exception {

		var messageQueue = new LinkedBlockingQueue<String>();
		var executorService = Executors.newScheduledThreadPool(4);

		executorService.scheduleAtFixedRate(
				() -> messageQueue.offer("hello @ " + Instant.now()), 0, 1,
				TimeUnit.SECONDS);

		var messageFlux = Flux.create(sink -> executorService.submit(() -> {
			while (true) {
				try {
					sink.next(messageQueue.take());
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		})).share();

		IntStream.range(0, 3).forEach(value -> messageFlux
				.subscribe(str -> log.info("client # " + value + " : " + str)));

		Thread.sleep(1000 * 5);
	}

	public static void shareASingleStream() throws Exception {

		var updates = Flux.interval(Duration.ofSeconds(1))
				.map(i -> "Hello @ " + Instant.now().toString());

		var broadcast = updates.share();

		class Client {

			Client(String clientId, Flux<String> updates) {
				updates.subscribe(str -> log.info(clientId + ':' + str));
			}

		}

		new Client("1", broadcast);
		new Client("2", broadcast);

		Thread.sleep(1000 * 20);
	}

}
