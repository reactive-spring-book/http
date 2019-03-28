package rsb.http.customers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class CustomerViewEndpointConfiguration {

	@Bean
	RouterFunction<ServerResponse> customerViews(CustomerRepository repository) {

		return route().GET("/fn/customers.php", r -> {
			var all = repository.findAll();
			var map = Map.of("customers", all, "type", "Functional Reactive");
			return ServerResponse.ok().render("customers", map);
		}).build();
	}

}
