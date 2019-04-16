package rsb.http.customers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
class CustomerViewController {

	private final CustomerRepository repository;

	CustomerViewController(CustomerRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/c/customers.php")
	String customersView(Model model) {//<1>
		var modelMap = Map.of("customers", repository.findAll(), //
			"type", "@Controller"); //<2>
		model.addAllAttributes(modelMap);//<3>
		return "customers";//<4>
	}
}
