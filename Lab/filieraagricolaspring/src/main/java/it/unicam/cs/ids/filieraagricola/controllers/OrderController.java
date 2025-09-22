package it.unicam.cs.ids.filieraagricola.controllers;

import it.unicam.cs.ids.filieraagricola.controllers.dto.OrderDto;
import it.unicam.cs.ids.filieraagricola.model.Order;
import it.unicam.cs.ids.filieraagricola.model.OrderStatus;
import it.unicam.cs.ids.filieraagricola.model.UserRole;
import it.unicam.cs.ids.filieraagricola.services.OrderService;
import it.unicam.cs.ids.filieraagricola.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;


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
    public ResponseEntity<Boolean> deleteOrder(@PathVariable String id) {
        if (!userService.hasRole(UserRole.PRODUCER)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Boolean done = orderService.deleteOrder(id);
        return ResponseEntity.ok(done);
    }
    @PostMapping()
    public ResponseEntity<Boolean> createOrder(@RequestBody OrderDto orderDto) {
        if (!(userService.hasRole(UserRole.PRODUCER)
                || userService.hasRole(UserRole.TRANSFORMER)
                || userService.hasRole(UserRole.DISTRIBUTOR))) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Boolean done = orderService.createOrder(orderDto);
        return ResponseEntity.ok(done);
    }







}
