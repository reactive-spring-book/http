package http;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;

interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {

}
