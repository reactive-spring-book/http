package rsb.http.customers;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class Customer {

	private String id, name;

	Customer(String i, String n) {
		this.id = i;
		this.name = n;
	}

	Customer(String name) {
		this.name = name;
	}
}
