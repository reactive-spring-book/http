package rsb.http.customers;

/*

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
*/

record Customer(String id, String name) {
}