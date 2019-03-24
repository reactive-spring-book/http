package rsb.http.routes;

import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.ServerRequest;

class CaseInsensitiveRequestPredicate implements RequestPredicate {

	private final RequestPredicate target;

	public static RequestPredicate i(RequestPredicate rp) {
		return new CaseInsensitiveRequestPredicate(rp);
	}

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
