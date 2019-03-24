package rsb.http.filters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
class LowercaseWebConfiguration {

	// for testing
	static String FMT = "Hello, %s!";

	@Bean
	RouterFunction<ServerResponse> routes() {
		return route(GET("/hi/{name}"), r -> ok().syncBody(String.format(FMT, r.pathVariable("name"))));
	}

	@Bean
	LowercaseWebFilter lowercaseWebFilter (){
		return new LowercaseWebFilter ();
	}
}
