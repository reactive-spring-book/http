package rsb.views;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import rsb.utils.IntervalMessageProducer;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Controller
class TickerSseController {

	@GetMapping("/ticker")
	String initialView() {
		return "ticker";
	}

	// <2>
	@GetMapping(produces = TEXT_EVENT_STREAM_VALUE, value = "/stream")
	String streamingUpdates(Model model) {
		var producer = IntervalMessageProducer.produce();
		var updates = new ReactiveDataDriverContextVariable(producer, 1); // <3>
		model.addAttribute("updates", updates);
		return "ticker :: #updateBlock"; // <4>
	}

}
