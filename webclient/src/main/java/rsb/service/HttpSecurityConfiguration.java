package rsb.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
class HttpSecurityConfiguration {

	// <1>
	@Bean
	MapReactiveUserDetailsService authentication() {
		var jlong = User.withDefaultPasswordEncoder()//
				.username("jlong")//
				.roles("USER")//
				.password("pw")// <2>
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
