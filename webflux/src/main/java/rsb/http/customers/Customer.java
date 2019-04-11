package rsb.http.customers;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
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
