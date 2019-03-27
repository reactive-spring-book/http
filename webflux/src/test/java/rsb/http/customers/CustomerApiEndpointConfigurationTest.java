package rsb.http.customers;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@WebFluxTest
@RunWith(SpringRunner.class)
@Import({ CustomerApiEndpointConfiguration.class, CustomerHandler.class })
public class CustomerApiEndpointConfigurationTest extends AbstractRestBaseClass {

	@Override
	String rootUrl() {
		return "/fn";
	}

}