package rsb.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "client")
class ClientProperties {

	private Http http = new Http();

	@Data

	public static class Http {

		private Basic basic = new Basic();

		private String rootUrl;

		@Data
		public static class Basic {

			private String username, password;

		}

	}

}
