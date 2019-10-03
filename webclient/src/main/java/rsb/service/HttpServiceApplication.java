package rsb.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
public class HttpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpServiceApplication.class, args);
	}

	// <1>
	@Bean
	MapReactiveUserDetailsService authentication() {
		var jlong = User.withDefaultPasswordEncoder()//
				.username("jlong")//
				.roles("USER")//
				.password("pw")//
				.build();
		return new MapReactiveUserDetailsService(jlong);
	}

	// <2>
	@Bean
	SecurityWebFilterChain authorization(ServerHttpSecurity http) {
		return http//
				.httpBasic(Customizer.withDefaults())//
				.csrf(ServerHttpSecurity.CsrfSpec::disable)//
				.authorizeExchange(spec -> spec//
						.pathMatchers("/greet/authenticated").authenticated()//
						.anyExchange().permitAll()//
				)//
				.build();
	}

}
