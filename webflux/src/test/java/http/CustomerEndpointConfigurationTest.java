package http;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@WebFluxTest
@RunWith(SpringRunner.class)
@Import({ CustomerEndpointConfiguration.class, CustomerHandler.class })
public class CustomerEndpointConfigurationTest extends AbstractRestBaseClass {

	@Override
	String rootUrl() {
		return "/fn";
	}

}