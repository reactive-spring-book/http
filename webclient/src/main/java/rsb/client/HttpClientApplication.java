package rsb.client;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Log4j2
@SpringBootApplication
@EnableConfigurationProperties(ClientProperties.class)
public class HttpClientApplication {

	@SneakyThrows
	public static void main(String[] args) {
		new SpringApplicationBuilder()//
				.sources(HttpClientApplication.class)//
				.profiles("client")//
				.run(args);
		Thread.sleep(20_000);
	}

}