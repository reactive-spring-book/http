package rsb.security.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@SpringBootApplication
public class ServiceApplication {

	public static void main(String args[]) {
		System.setProperty("spring.profiles.active", "service");
		SpringApplication.run(ServiceApplication.class, args);
	}

	@Bean
	SecurityWebFilterChain authorization(ServerHttpSecurity http) {
		return http.httpBasic(Customizer.withDefaults()).authorizeExchange(ae -> ae//
				.pathMatchers("/greetings").authenticated()//
				.anyExchange().permitAll()//
		).csrf(ServerHttpSecurity.CsrfSpec::disable).build();
	}

	@Bean
	MapReactiveUserDetailsService authentication() {
		return new MapReactiveUserDetailsService(User.withDefaultPasswordEncoder()
				.username("jlong").password("pw").roles("USER").build());
	}

}

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