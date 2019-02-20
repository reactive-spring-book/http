package http;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;

@WebFluxTest
@RunWith(SpringRunner.class)
public class CustomerRestControllerTest extends AbstractRestBaseClass {

	@Override
	String rootUrl() {
		return "/rc";
	}


}