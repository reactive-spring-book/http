package rsb.http.routes;

import org.springframework.http.server.PathContainer;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.support.ServerRequestWrapper;

import java.net.URI;

public class LowercaseUriServerRequestWrapper extends ServerRequestWrapper {

	public LowercaseUriServerRequestWrapper(ServerRequest target) {
		super(target);
	}

	// <1>
	@Override
	public URI uri() {
		return URI.create(super.uri().toString().toLowerCase());
	}

	@Override
	public String path() {
		return uri().getRawPath();
	}

	@Override
	public PathContainer pathContainer() {
		return PathContainer.parsePath(path());
	}

}
