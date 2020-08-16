package rsb.security.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
class SecurityConfiguration {

	// <1>
	@Bean
	MapReactiveUserDetailsService authentication() {
		UserDetails jlong = User.withDefaultPasswordEncoder().username("jlong")
				.password("pw").roles("USER").build();
		UserDetails rwinch = User.withDefaultPasswordEncoder().username("rwinch")
				.password("pw").roles("USER", "ADMIN").build();
		return new MapReactiveUserDetailsService(jlong, rwinch);
	}

	// <2>
	@Bean
	SecurityWebFilterChain authorization(ServerHttpSecurity http) {
		return http//
				.httpBasic(Customizer.withDefaults())//
				.authorizeExchange(ae -> ae//
						.pathMatchers("/greetings").authenticated()//
						.anyExchange().permitAll()//
				)//
				.csrf(ServerHttpSecurity.CsrfSpec::disable)//
				.build();
	}

}
