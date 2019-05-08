package rsb.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Log4j2
@SpringBootApplication
public class HttpClientApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder().sources(HttpClientApplication.class)
				.profiles("client").run(args);
	}

}