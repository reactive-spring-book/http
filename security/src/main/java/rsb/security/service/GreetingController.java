package rsb.security.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@RestController
class GreetingController {

	@GetMapping("/greetings")
	Mono<Map<String, String>> greet(@AuthenticationPrincipal Mono<UserDetails> user) {
		return user//
				.map(UserDetails::getUsername)//
				.map(name -> Map.of("greetings",
						"Hello " + name + " @ " + Instant.now() + "!"));
	}

}
