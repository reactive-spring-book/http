package rsb.views;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import rsb.utils.IntervalMessageProducer;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Controller
class CustomerController {

	@GetMapping(produces = TEXT_EVENT_STREAM_VALUE, value = "/stream.php")
	String timer(Model model) {
		var producer = IntervalMessageProducer.produce();
		var updates = new ReactiveDataDriverContextVariable(producer, 1);
		model.addAttribute("updates", updates);
		return "ticker :: #updateBlock";
	}

	@GetMapping("/ticker.php")
	String timer() {
		return "ticker";
	}

}
