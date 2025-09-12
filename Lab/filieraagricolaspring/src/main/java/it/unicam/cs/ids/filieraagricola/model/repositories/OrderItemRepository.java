package it.unicam.cs.ids.filieraagricola.model.repositories;

import it.unicam.cs.ids.filieraagricola.model.OrderItem;
import org.springframework.data.repository.ListCrudRepository;

public interface OrderItemRepository extends ListCrudRepository<OrderItem, String> {
}
