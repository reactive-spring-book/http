package http;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@NoArgsConstructor
@Data
@Document
class Customer {

	private String id;

	private String name;

	Customer(String i, String n) {
		this.id = i;
		this.name = n;
	}

	Customer(UUID uid, String name) {
		this.id = uid.toString();
		this.name = name;
	}

	Customer(String name) {
		this.name = name;
	}

}
