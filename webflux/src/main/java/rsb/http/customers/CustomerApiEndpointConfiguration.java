package rsb.http.customers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class CustomerApiEndpointConfiguration {

	@Bean
	RouterFunction<ServerResponse> customerApis(CustomerHandler handler) {
		return route().nest(path("/fn/customers"),
				builder -> builder.GET("/{id}", handler::handleFindCustomerById)
						.GET("", handler::handleFindAll)
						.POST("", handler::handleCreateCustomer))
				.build();
	}

}
