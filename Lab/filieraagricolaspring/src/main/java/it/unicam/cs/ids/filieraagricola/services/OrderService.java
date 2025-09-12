package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.model.Order;
import it.unicam.cs.ids.filieraagricola.model.OrderItem;
import it.unicam.cs.ids.filieraagricola.model.OrderStatus;
import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.model.repositories.OrderItemRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.OrderRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {


    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;


    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findByOrderStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> findByBuyer(String buyerId) {
        Optional<User> opt = userRepository.findById(buyerId);
        if (opt.isPresent()) {
            return orderRepository.findByBuyer(opt.get());
        }
        return new LinkedList<>();
    }


    public List<Order> findBySeller(String sellerId) {
        Optional<User> opt = userRepository.findById(sellerId);
        if (opt.isPresent()) {
            return orderRepository.findBySeller(opt.get());
        }
        return new LinkedList<>();
    }


    //TODO Create dell'order



    public boolean deleteOrder(String id) {
        Optional<Order> opt = orderRepository.findById(id);
        if (opt.isPresent()) {
            Order order = opt.get();
            List<OrderItem> items = new LinkedList<>();
            items.addAll(order.getOrderItems());
            order.getOrderItems().clear();
            order = orderRepository.save(order);
            for (OrderItem i : items ) {
                orderItemRepository.delete(i);
            }
            orderRepository.delete(order);
            return true;
        }
        return false;
     }





}
