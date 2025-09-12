package it.unicam.cs.ids.filieraagricola.controllers;

import it.unicam.cs.ids.filieraagricola.model.Order;
import it.unicam.cs.ids.filieraagricola.model.OrderStatus;
import it.unicam.cs.ids.filieraagricola.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;



    @GetMapping
    public List<Order> findAll() {
        return orderService.findAll();
    }
    @GetMapping("/findByStatus/{status}")
    public List<Order> findByOrderStatus(@PathVariable OrderStatus status) {
        return orderService.findByOrderStatus(status);
    }
    @GetMapping("/findByBuyer/{buyerId}")
    public List<Order> findByBuyer(@PathVariable String buyerId) {
        return orderService.findByBuyer(buyerId);
    }
    @GetMapping("/findBySeller/{sellerId}")
    public List<Order> findBySeller(@PathVariable String sellerId) {
        return orderService.findBySeller(sellerId);
    }
    @DeleteMapping("/{id}")
    public boolean deleteOrder(@PathVariable String id) {
        return orderService.deleteOrder(id);
    }







}
