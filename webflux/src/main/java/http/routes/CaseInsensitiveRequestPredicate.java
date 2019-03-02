package http.routes;

import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.ServerRequest;

class CaseInsensitiveRequestPredicate implements RequestPredicate {

	public static RequestPredicate i(RequestPredicate rp) {
		return new CaseInsensitiveRequestPredicate(rp);
	}

	private final RequestPredicate target;

	CaseInsensitiveRequestPredicate(RequestPredicate target) {
		this.target = target;
	}

	@Override
	public boolean test(ServerRequest request) { // <1>
		return this.target.test(new LowercaseUriServerRequestWrapper(request));
	}

	@Override
	public String toString() {
		return this.target.toString();
	}

}
