package hateoas.hal;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

@Data
@RequiredArgsConstructor
class Customer implements Comparable<Customer> {

	private String id = UUID.randomUUID().toString();

	private final String name;

	private final Set<Order> orders = new ConcurrentSkipListSet<>();

	public void addOrder(String sku, Date d) {
		this.orders.add(new Order(sku, d));
	}

	Customer(String n, Order... orders) {
		this.name = n;
		for (Order o : orders) {
			this.addOrder(o.getId(), o.getWhen());
		}
	}

	@Override
	public int compareTo(Customer o) {
		return this.id.compareTo(o.id);
	}

}
