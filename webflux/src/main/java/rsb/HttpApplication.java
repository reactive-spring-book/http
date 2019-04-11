package rsb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HttpApplication {

	public static void main(String args[]) {
		System.setProperty("spring.main.lazy-initialization", "false");
		SpringApplication.run(HttpApplication.class, args);
	}

}
