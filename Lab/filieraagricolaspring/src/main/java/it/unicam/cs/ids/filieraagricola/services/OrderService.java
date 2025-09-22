package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.controllers.dto.OrderDto;
import it.unicam.cs.ids.filieraagricola.controllers.dto.OrderItemDto;
import it.unicam.cs.ids.filieraagricola.model.*;
import it.unicam.cs.ids.filieraagricola.model.repositories.OrderItemRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.OrderRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.ProductRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service that manages Order lifecycle.
 *
 * <p>This service uses Spring Data repositories to persist orders and order items.
 * Methods are transactional where a group of operations must succeed or fail together.</p>
 */
@Service
public class OrderService {

    public static final int DEFAULT_DELIVERY_TIME = 1000 * 3600 * 24 * 4;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ClientHttpRequestFactorySettings clientHttpRequestFactorySettings;

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

    public boolean createOrder(OrderDto orderDto) {
        Optional<User> optBuyer = userRepository.findById(orderDto.getBuyerId());
        if (optBuyer.isEmpty()) {
            return false;
        }
        Optional<User> optSeller = userRepository.findById(orderDto.getSellerId());
        if (optSeller.isEmpty()) {
            return false;
        }
        List<OrderItem> items = new LinkedList<>();
        double totalAmount = 0;
        for (OrderItemDto dto : orderDto.getOrderItemDtoList()) {
            Optional<Product> opt = productRepository.findById(dto.getProductId());
            if (opt.isPresent()) {
                Product product = opt.get();
                OrderItem item = new OrderItem();
                item.setId(UUID.randomUUID().toString());
                item.setQuantity(dto.getQuantity());
                item.setNotes(dto.getNotes());
                item.setProduct(product);
                orderItemRepository.save(item);
                items.add(item);
                double amount = item.getQuantity() * product.getPrice();
                totalAmount += amount;
            }
        }

        Order order = new Order();
        order.setBuyer(optBuyer.get());
        order.setSeller(optSeller.get());
        order.setOrderItems(items);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PROCESSING);
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setExpectedDeliveryDate(new Timestamp(System.currentTimeMillis() + DEFAULT_DELIVERY_TIME));
        order.setActualDeliveryDate(order.getExpectedDeliveryDate());
        order.setDeliveryAddress(orderDto.getDeliveryAddress());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setDeliveryMethod(orderDto.getDeliveryMethod());
        order.setNotes(orderDto.getNotes());
        orderRepository.save(order);
        return true;
    }



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