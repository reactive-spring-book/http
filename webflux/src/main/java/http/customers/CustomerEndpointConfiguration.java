package http.customers;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Log4j2
class CustomerEndpointConfiguration {

	@Bean
	RouterFunction<ServerResponse> routes(CustomerHandler handler) {

		RouterFunction<ServerResponse> routes = //
				route(GET("/{id}"), handler::handleFindCustomerById)
						.andRoute(method(HttpMethod.GET), handler::handleFindAll)
						.andRoute(method(HttpMethod.POST), handler::handleCreateCustomer);

		return RouterFunctions.nest(path("/fn/customers"), routes);
	}

}
