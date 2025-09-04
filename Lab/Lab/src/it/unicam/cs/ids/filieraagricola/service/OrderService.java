package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Order;
import it.unicam.cs.ids.filieraagricola.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing orders in the agricultural supply chain platform.
 *
 * <p>This service handles the complete order lifecycle including creation, status updates,
 * tracking, and reporting. It uses defensive copying to ensure data integrity and follows
 * the same patterns as other services in the platform.</p>
 *
 * <p>All orders are stored internally and defensive copies are returned to maintain
 * encapsulation. The service supports operations like order creation, status management,
 * search, filtering, and analytics.</p>
 */
public class OrderService {

    private final List<Order> orderList;
    private final Map<OrderStatus, List<Order>> ordersByStatus;
    private final Map<String, List<Order>> ordersByBuyer;
    private final Map<String, List<Order>> ordersBySeller;

    /**
     * Constructs an empty OrderService.
     */
    public OrderService() {
        this.orderList = new ArrayList<>();
        this.ordersByStatus = new EnumMap<>(OrderStatus.class);
        this.ordersByBuyer = new HashMap<>();
        this.ordersBySeller = new HashMap<>();

        // Initialize lists for each order status
        for (OrderStatus status : OrderStatus.values()) {
            ordersByStatus.put(status, new ArrayList<>());
        }
    }

    /**
     * Creates a new order in the system.
     *
     * <p>The order is stored as a defensive copy and added to various indexes
     * for efficient retrieval by status, buyer, and seller.</p>
     *
     * @param order the order to create (must not be null)
     * @return defensive copy of the created order
     * @throws IllegalArgumentException if order is null or already exists
     */
    public Order createOrder(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");

        // Check if order already exists
        if (findOrderById(order.getId()).isPresent()) {
            throw new IllegalArgumentException("Order with ID " + order.getId() + " already exists");
        }

        Order orderCopy = copyOrder(order);
        addOrderToIndexes(orderCopy);

        return copyOrder(orderCopy);
    }

    /**
     * Finds an order by its unique identifier.
     *
     * @param orderId the order ID to search for (must not be null)
     * @return Optional containing a defensive copy of the found order or empty if not found
     * @throws IllegalArgumentException if orderId is null
     */
    public Optional<Order> findOrderById(String orderId) {
        Objects.requireNonNull(orderId, "Order ID cannot be null");

        return orderList.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .map(this::copyOrder);
    }

    /**
     * Returns all orders for a specific buyer.
     *
     * @param buyerId the buyer ID (must not be null)
     * @return list of defensive copies of orders for the specified buyer
     * @throws IllegalArgumentException if buyerId is null
     */
    public List<Order> getOrdersByBuyer(String buyerId) {
        Objects.requireNonNull(buyerId, "Buyer ID cannot be null");

        return ordersByBuyer.getOrDefault(buyerId, Collections.emptyList()).stream()
                .map(this::copyOrder)
                .collect(Collectors.toList());
    }

    /**
     * Returns all orders for a specific seller.
     *
     * @param sellerId the seller ID (must not be null)
     * @return list of defensive copies of orders for the specified seller
     * @throws IllegalArgumentException if sellerId is null
     */
    public List<Order> getOrdersBySeller(String sellerId) {
        Objects.requireNonNull(sellerId, "Seller ID cannot be null");

        return ordersBySeller.getOrDefault(sellerId, Collections.emptyList()).stream()
                .map(this::copyOrder)
                .collect(Collectors.toList());
    }

    /**
     * Returns all orders with a specific status.
     *
     * @param status the order status (must not be null)
     * @return list of defensive copies of orders with the specified status
     * @throws IllegalArgumentException if status is null
     */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        Objects.requireNonNull(status, "Order status cannot be null");

        return ordersByStatus.get(status).stream()
                .map(this::copyOrder)
                .collect(Collectors.toList());
    }

    /**
     * Updates the status of an existing order.
     *
     * @param orderId the order ID (must not be null)
     * @param newStatus the new status (must not be null)
     * @return true if the order status was successfully updated
     * @throws IllegalArgumentException if orderId or newStatus is null, or order not found
     */
    public boolean updateOrderStatus(String orderId, OrderStatus newStatus) {
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        Objects.requireNonNull(newStatus, "New status cannot be null");

        Optional<Integer> indexOpt = findOrderIndex(orderId);
        if (indexOpt.isEmpty()) {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found");
        }

        int index = indexOpt.get();
        Order order = orderList.get(index);
        OrderStatus oldStatus = order.getStatus();

        // Update the order status
        order.setStatus(newStatus);

        // Update status indexes
        ordersByStatus.get(oldStatus).remove(order);
        ordersByStatus.get(newStatus).add(order);

        return true;
    }

    /**
     * Updates an existing order's information.
     *
     * @param updatedOrder the order with updated information (must not be null)
     * @return true if the order was successfully updated
     * @throws IllegalArgumentException if order is null or not found
     */
    public boolean updateOrder(Order updatedOrder) {
        Objects.requireNonNull(updatedOrder, "Updated order cannot be null");

        Optional<Integer> indexOpt = findOrderIndex(updatedOrder.getId());
        if (indexOpt.isEmpty()) {
            throw new IllegalArgumentException("Order with ID " + updatedOrder.getId() + " not found");
        }

        int index = indexOpt.get();
        Order oldOrder = orderList.get(index);
        Order newOrderCopy = copyOrder(updatedOrder);

        // Remove from old indexes
        removeOrderFromIndexes(oldOrder);

        // Update in main list
        orderList.set(index, newOrderCopy);

        // Add to new indexes
        addOrderToIndexes(newOrderCopy);

        return true;
    }

    /**
     * Cancels an order by setting its status to CANCELLED.
     *
     * @param orderId the order ID (must not be null)
     * @return true if the order was successfully cancelled
     * @throws IllegalArgumentException if orderId is null or order not found or already delivered
     */
    public boolean cancelOrder(String orderId) {
        Objects.requireNonNull(orderId, "Order ID cannot be null");

        Optional<Order> orderOpt = findOrderById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found");
        }

        Order order = orderOpt.get();
        if (order.isDelivered()) {
            throw new IllegalArgumentException("Cannot cancel a delivered order");
        }

        return updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    /**
     * Confirms a pending order by setting its status to CONFIRMED.
     *
     * @param orderId the order ID (must not be null)
     * @return true if the order was successfully confirmed
     * @throws IllegalArgumentException if orderId is null or order not found or not pending
     */
    public boolean confirmOrder(String orderId) {
        Objects.requireNonNull(orderId, "Order ID cannot be null");

        Optional<Order> orderOpt = findOrderById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found");
        }

        Order order = orderOpt.get();
        if (!order.isPending()) {
            throw new IllegalArgumentException("Only pending orders can be confirmed");
        }

        return updateOrderStatus(orderId, OrderStatus.CONFIRMED);
    }

    /**
     * Marks an order as shipped by setting its status to SHIPPED.
     *
     * @param orderId the order ID (must not be null)
     * @return true if the order was successfully marked as shipped
     * @throws IllegalArgumentException if orderId is null or order not found or not in processing
     */
    public boolean shipOrder(String orderId) {
        Objects.requireNonNull(orderId, "Order ID cannot be null");

        Optional<Order> orderOpt = findOrderById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found");
        }

        Order order = orderOpt.get();
        if (!order.isProcessing()) {
            throw new IllegalArgumentException("Only processing orders can be shipped");
        }

        return updateOrderStatus(orderId, OrderStatus.SHIPPED);
    }

    /**
     * Marks an order as delivered by setting its status to DELIVERED.
     *
     * @param orderId the order ID (must not be null)
     * @return true if the order was successfully marked as delivered
     * @throws IllegalArgumentException if orderId is null or order not found or not shipped
     */
    public boolean deliverOrder(String orderId) {
        Objects.requireNonNull(orderId, "Order ID cannot be null");

        Optional<Order> orderOpt = findOrderById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found");
        }

        Order order = orderOpt.get();
        if (!order.isShipped()) {
            throw new IllegalArgumentException("Only shipped orders can be delivered");
        }

        return updateOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    /**
     * Returns all orders.
     *
     * @return list of defensive copies of all orders
     */
    public List<Order> getAllOrders() {
        return orderList.stream()
                .map(this::copyOrder)
                .collect(Collectors.toList());
    }

    /**
     * Returns orders placed within a specific date range.
     *
     * @param startDate the start date (inclusive, must not be null)
     * @param endDate the end date (inclusive, must not be null)
     * @return list of defensive copies of orders within the date range
     * @throws IllegalArgumentException if startDate or endDate is null, or startDate is after endDate
     */
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Objects.requireNonNull(startDate, "Start date cannot be null");
        Objects.requireNonNull(endDate, "End date cannot be null");

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return orderList.stream()
                .filter(order -> !order.getOrderDate().isBefore(startDate) &&
                        !order.getOrderDate().isAfter(endDate))
                .map(this::copyOrder)
                .collect(Collectors.toList());
    }

    /**
     * Returns orders with total amount within a specific range.
     *
     * @param minAmount the minimum amount (inclusive, must be >= 0)
     * @param maxAmount the maximum amount (inclusive, must be >= minAmount)
     * @return list of defensive copies of orders within the amount range
     * @throws IllegalArgumentException if amounts are invalid
     */
    public List<Order> getOrdersByAmountRange(double minAmount, double maxAmount) {
        if (minAmount < 0) {
            throw new IllegalArgumentException("Minimum amount cannot be negative");
        }
        if (maxAmount < minAmount) {
            throw new IllegalArgumentException("Maximum amount cannot be less than minimum amount");
        }

        return orderList.stream()
                .filter(order -> order.getTotalAmount() >= minAmount &&
                        order.getTotalAmount() <= maxAmount)
                .map(this::copyOrder)
                .collect(Collectors.toList());
    }

    /**
     * Returns orders that are overdue (past expected delivery date and not delivered/cancelled).
     *
     * @return list of defensive copies of overdue orders
     */
    public List<Order> getOverdueOrders() {
        return orderList.stream()
                .filter(Order::isOverdue)
                .map(this::copyOrder)
                .collect(Collectors.toList());
    }

    /**
     * Returns orders that contain organic certified products.
     *
     * @return list of defensive copies of organic certified orders
     */
    public List<Order> getOrganicCertifiedOrders() {
        return orderList.stream()
                .filter(Order::isOrganicCertified)
                .map(this::copyOrder)
                .collect(Collectors.toList());
    }

    /**
     * Returns the count of orders by status.
     *
     * @return map containing the count of orders for each status
     */
    public Map<OrderStatus, Long> getOrderCountByStatus() {
        return orderList.stream()
                .collect(Collectors.groupingBy(
                        Order::getStatus,
                        () -> new EnumMap<>(OrderStatus.class),
                        Collectors.counting()
                ));
    }

    /**
     * Calculates the total revenue from all delivered orders.
     *
     * @return total revenue from delivered orders
     */
    public double getTotalRevenue() {
        return orderList.stream()
                .filter(Order::isDelivered)
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }

    /**
     * Calculates the total revenue for a specific seller from delivered orders.
     *
     * @param sellerId the seller ID (must not be null)
     * @return total revenue for the seller
     * @throws IllegalArgumentException if sellerId is null
     */
    public double getTotalRevenueForSeller(String sellerId) {
        Objects.requireNonNull(sellerId, "Seller ID cannot be null");

        return orderList.stream()
                .filter(order -> order.getSellerId().equals(sellerId) && order.isDelivered())
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }

    /**
     * Returns the average order value.
     *
     * @return average order value, or 0 if no orders
     */
    public double getAverageOrderValue() {
        return orderList.stream()
                .mapToDouble(Order::getTotalAmount)
                .average()
                .orElse(0.0);
    }

    // ----------------- private helpers -----------------

    /**
     * Creates a defensive copy of an order using the clone method.
     *
     * @param order the order to copy (must not be null)
     * @return a cloned copy of the order
     */
    private Order copyOrder(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        return order.clone();
    }

    /**
     * Adds an order to all relevant indexes.
     *
     * @param order the order to add to indexes
     */
    private void addOrderToIndexes(Order order) {
        orderList.add(order);
        ordersByStatus.get(order.getStatus()).add(order);
        ordersByBuyer.computeIfAbsent(order.getBuyerId(), k -> new ArrayList<>()).add(order);
        ordersBySeller.computeIfAbsent(order.getSellerId(), k -> new ArrayList<>()).add(order);
    }

    /**
     * Removes an order from all relevant indexes.
     *
     * @param order the order to remove from indexes
     */
    private void removeOrderFromIndexes(Order order) {
        ordersByStatus.get(order.getStatus()).remove(order);
        ordersByBuyer.get(order.getBuyerId()).remove(order);
        ordersBySeller.get(order.getSellerId()).remove(order);
    }

    /**
     * Finds the index of an order in the main list by ID.
     *
     * @param orderId the order ID to search for
     * @return Optional containing the index or empty if not found
     */
    private Optional<Integer> findOrderIndex(String orderId) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getId().equals(orderId)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }
}
