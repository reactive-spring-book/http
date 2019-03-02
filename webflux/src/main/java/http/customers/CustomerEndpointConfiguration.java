package http.customers;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.Duration;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Log4j2
@Configuration
class CustomerEndpointConfiguration {

	@Bean
	RouterFunction<ServerResponse> customerApiEndpoints(CustomerHandler handler) {

		RouterFunction<ServerResponse> routes = //
				route(GET("/{id}"), handler::handleFindCustomerById)
						.andRoute(method(HttpMethod.GET), handler::handleFindAll)
						.andRoute(method(HttpMethod.POST), handler::handleCreateCustomer);

		return RouterFunctions.nest(path("/fn/customers"), routes);
	}

	@Bean
	RouterFunction<ServerResponse> customerViewEndpoints(CustomerRepository repository) {
		return route(GET("/customers.php"), request -> {
			var all = repository.findAll();
			var map = Map.of("customers", all);
			return ServerResponse.ok().render("customers", map);
		});
	}

}
