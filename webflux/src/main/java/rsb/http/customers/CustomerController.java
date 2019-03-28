package rsb.http.customers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class CustomerController {

	private final CustomerRepository repository;

	CustomerController(CustomerRepository repository) {
		this.repository = repository;
	}

	@GetMapping(value = "/rc/customers.php")
	String customersView(Model model) {
		var all = repository.findAll();
		model.addAllAttributes(Map.of("type", "Controller", "customers", all));
		return "customers";
	}

}
