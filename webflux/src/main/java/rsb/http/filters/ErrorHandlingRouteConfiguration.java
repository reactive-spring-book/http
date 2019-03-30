package rsb.http.filters;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;

@Configuration
class ErrorHandlingRouteConfiguration {

	@Bean
	RouterFunction<ServerResponse> errors() {
		var productIdPathVariable = "productId";
		return route() //
			.GET("/products/{" + productIdPathVariable + "}", request -> {
			var productId = request.pathVariable(productIdPathVariable);
			if (!Set.of("1", "2").contains(productId)) {
				return ServerResponse.ok().syncBody(new Product(productId));
			}
			else {
				return Mono.error(new ProductNotFoundException(productId));
			}
		}) //
			.filter((request, next) -> next.handle(request) // <1>
				.onErrorResume(ProductNotFoundException.class, pnfe -> notFound().build())) // <2>
				.build();
	}

}

@Data
@RequiredArgsConstructor
class Product {

	private final String id;
}

@Data
@RequiredArgsConstructor
class ProductNotFoundException extends RuntimeException {

	private final String productId;

}
