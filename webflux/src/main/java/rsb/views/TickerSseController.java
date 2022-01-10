package rsb.views;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;
import static rsb.utils.IntervalMessageProducer.produce;

@Controller
class TickerSseController {

	// <1>
	@GetMapping("/ticker.php")
	String initialView() {
		return "ticker";
	}

	// <2>
	@GetMapping(produces = TEXT_EVENT_STREAM_VALUE, value = "/ticker-stream")
	String streamingUpdates(Model model) {
		var updates = new ReactiveDataDriverContextVariable(produce(), 1); // <3>
		model.addAttribute("updates", updates);
		return "ticker :: #updateBlock"; // <4>
	}

}
