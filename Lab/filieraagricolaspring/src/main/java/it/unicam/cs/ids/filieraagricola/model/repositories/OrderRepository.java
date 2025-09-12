package it.unicam.cs.ids.filieraagricola.model.repositories;

import it.unicam.cs.ids.filieraagricola.model.Order;
import it.unicam.cs.ids.filieraagricola.model.OrderStatus;
import it.unicam.cs.ids.filieraagricola.model.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface OrderRepository extends ListCrudRepository<Order, String> {


     List<Order> findBySeller(User seller);
     List<Order> findByBuyer(User buyer);
     List<Order> findByStatus(OrderStatus status);













}
