package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Order;
import it.unicam.cs.ids.filieraagricola.model.Order.OrderItem;
import it.unicam.cs.ids.filieraagricola.model.OrderStatus;
import it.unicam.cs.ids.filieraagricola.service.exception.NotFoundException;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Service class for managing orders in the agricultural supply chain platform.
 *
 * <p>This service handles the complete order lifecycle including creation, status updates,
 * tracking, and reporting. It ensures data integrity through validation and manages
 * order state transitions. Order objects are treated as immutable data transfer objects
 * once created, with updates resulting in new instances or modifications handled internally by the service.</p>
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
     * @param buyerId            Buyer identifier.
     * @param sellerId           Seller identifier.
     * @param orderItems         List of ordered items.
     * @param expectedDeliveryDate Expected delivery date.
     * @param deliveryAddress    Delivery address.
     * @param paymentMethod      Payment method.
     * @param deliveryMethod     Delivery method.
     * @param notes              Additional notes.
     * @param organicCertified   Whether the order qualifies for organic certification.
     * @return The newly created Order instance.
     * @throws ValidationException If any validation fails.
     */
    public Order createOrder(String buyerId, String sellerId, List<OrderItem> orderItems,
                             LocalDateTime expectedDeliveryDate, String deliveryAddress,
                             String paymentMethod, String deliveryMethod, String notes,
                             boolean organicCertified) {
        validateOrderCreationData(buyerId, sellerId, orderItems);

        double totalAmount = calculateTotalAmount(orderItems);
        LocalDateTime orderDate = LocalDateTime.now();
        OrderStatus status = OrderStatus.PENDING;

        Order newOrder = new Order(
                buyerId.trim(), sellerId.trim(), orderItems, totalAmount, status, orderDate,
                expectedDeliveryDate, null, // actualDeliveryDate is null initially
                deliveryAddress != null ? deliveryAddress.trim() : null,
                paymentMethod != null ? paymentMethod.trim() : null,
                notes != null ? notes.trim() : null,
                organicCertified,
                deliveryMethod != null ? deliveryMethod.trim() : null
        );

        addOrderToIndexes(newOrder);
        return newOrder;
    }

    /**
     * Finds an order by its unique identifier.
     *
     * @param orderId The order ID to search for.
     * @return An Optional containing the found order or empty if not found.
     * @throws ValidationException If orderId is null or empty.
     */
    public Optional<Order> findOrderById(String orderId) {
        validateId(orderId);
        return orderList.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst();
    }

    /**
     * Returns all orders for a specific buyer.
     *
     * @param buyerId The buyer ID.
     * @return An unmodifiable list of orders for the specified buyer.
     * @throws ValidationException If buyerId is null or empty.
     */
    public List<Order> getOrdersByBuyer(String buyerId) {
        validateBuyerId(buyerId);
        return Collections.unmodifiableList(ordersByBuyer.getOrDefault(buyerId, Collections.emptyList()));
    }

    /**
     * Returns all orders for a specific seller.
     *
     * @param sellerId The seller ID.
     * @return An unmodifiable list of orders for the specified seller.
     * @throws ValidationException If sellerId is null or empty.
     */
    public List<Order> getOrdersBySeller(String sellerId) {
        validateSellerId(sellerId);
        return Collections.unmodifiableList(ordersBySeller.getOrDefault(sellerId, Collections.emptyList()));
    }

    /**
     * Returns all orders with a specific status.
     *
     * @param status The order status.
     * @return An unmodifiable list of orders with the specified status.
     * @throws ValidationException If status is null.
     */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        Objects.requireNonNull(status, "Order status cannot be null.");
        return Collections.unmodifiableList(ordersByStatus.getOrDefault(status, Collections.emptyList()));
    }

    /**
     * Updates the status of an existing order.
     *
     * @param orderId   The order ID.
     * @param newStatus The new status.
     * @return True if the order status was successfully updated.
     * @throws ValidationException If orderId or newStatus is null/empty, or order not found.
     * @throws NotFoundException If the order is not found.
     */
    public boolean updateOrderStatus(String orderId, OrderStatus newStatus) {
        validateId(orderId);
        Objects.requireNonNull(newStatus, "New status cannot be null.");

        Optional<Integer> indexOpt = findOrderIndex(orderId);
        if (indexOpt.isEmpty()) {
            throw new NotFoundException("Order with ID " + orderId + " not found.");
        }

        int index = indexOpt.get();
        Order oldOrder = orderList.get(index);
        OrderStatus oldStatus = oldOrder.getStatus();

        if (oldStatus == newStatus) {
            return false; // No change needed
        }

        LocalDateTime actualDeliveryDate = oldOrder.getActualDeliveryDate();
        if (newStatus == OrderStatus.DELIVERED && actualDeliveryDate == null) {
            actualDeliveryDate = LocalDateTime.now();
        } else if (newStatus != OrderStatus.DELIVERED) {
            actualDeliveryDate = null; // Clear actual delivery date if status changes from DELIVERED
        }

        Order updatedOrder = new Order(
                oldOrder.getId(), oldOrder.getBuyerId(), oldOrder.getSellerId(), oldOrder.getOrderItems(),
                oldOrder.getTotalAmount(), newStatus, oldOrder.getOrderDate(), oldOrder.getExpectedDeliveryDate(),
                actualDeliveryDate, oldOrder.getDeliveryAddress(), oldOrder.getPaymentMethod(), oldOrder.getNotes(),
                oldOrder.isOrganicCertified(), oldOrder.getDeliveryMethod()
        );

        // Update status indexes
        ordersByStatus.get(oldStatus).remove(oldOrder);
        ordersByStatus.get(newStatus).add(updatedOrder);
        orderList.set(index, updatedOrder); // Update in main list

        return true;
    }

    /**
     * Updates an existing order's information.
     *
     * @param orderId            The ID of the order to update.
     * @param buyerId            New buyer identifier.
     * @param sellerId           New seller identifier.
     * @param orderItems         New list of ordered items.
     * @param status             New order status.
     * @param expectedDeliveryDate New expected delivery date.
     * @param actualDeliveryDate New actual delivery date.
     * @param deliveryAddress    New delivery address.
     * @param paymentMethod      New payment method.
     * @param notes              New additional notes.
     * @param organicCertified   New organic certification status.
     * @param deliveryMethod     New delivery method.
     * @return The updated Order instance.
     * @throws ValidationException If any validation fails.
     * @throws NotFoundException If the order is not found.
     */
    public Order updateOrder(String orderId, String buyerId, String sellerId, List<OrderItem> orderItems,
                             OrderStatus status, LocalDateTime expectedDeliveryDate, LocalDateTime actualDeliveryDate,
                             String deliveryAddress, String paymentMethod, String notes,
                             boolean organicCertified, String deliveryMethod) {
        validateId(orderId);
        validateOrderCreationData(buyerId, sellerId, orderItems);
        Objects.requireNonNull(status, "Order status cannot be null.");

        Optional<Integer> indexOpt = findOrderIndex(orderId);
        if (indexOpt.isEmpty()) {
            throw new NotFoundException("Order with ID " + orderId + " not found.");
        }

        int index = indexOpt.get();
        Order oldOrder = orderList.get(index);

        double newTotalAmount = calculateTotalAmount(orderItems);

        Order updatedOrder = new Order(
                orderId, buyerId.trim(), sellerId.trim(), orderItems, newTotalAmount, status, oldOrder.getOrderDate(),
                expectedDeliveryDate, actualDeliveryDate,
                deliveryAddress != null ? deliveryAddress.trim() : null,
                paymentMethod != null ? paymentMethod.trim() : null,
                notes != null ? notes.trim() : null,
                organicCertified,
                deliveryMethod != null ? deliveryMethod.trim() : null
        );

        // Remove from old indexes and add to new ones if buyer/seller/status changed
        removeOrderFromIndexes(oldOrder);
        addOrderToIndexes(updatedOrder);
        orderList.set(index, updatedOrder); // Update in main list

        return updatedOrder;
    }

    /**
     * Cancels an order by setting its status to CANCELLED.
     *
     * @param orderId The order ID.
     * @return True if the order was successfully cancelled.
     * @throws ValidationException If orderId is null/empty, order not found, or already delivered.
     * @throws NotFoundException If the order is not found.
     */
    public boolean cancelOrder(String orderId) {
        Optional<Order> orderOpt = findOrderById(orderId);
        if (orderOpt.isEmpty()) {
            throw new NotFoundException("Order with ID " + orderId + " not found.");
        }
        if (isDelivered(orderOpt.get())) {
            throw new ValidationException("Cannot cancel a delivered order.");
        }
        return updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    /**
     * Confirms a pending order by setting its status to CONFIRMED.
     *
     * @param orderId The order ID.
     * @return True if the order was successfully confirmed.
     * @throws ValidationException If orderId is null/empty, order not found, or not pending.
     * @throws NotFoundException If the order is not found.
     */
    public boolean confirmOrder(String orderId) {
        Optional<Order> orderOpt = findOrderById(orderId);
        if (orderOpt.isEmpty()) {
            throw new NotFoundException("Order with ID " + orderId + " not found.");
        }
        if (!isPending(orderOpt.get())) {
            throw new ValidationException("Only pending orders can be confirmed.");
        }
        return updateOrderStatus(orderId, OrderStatus.CONFIRMED);
    }

    /**
     * Marks an order as shipped by setting its status to SHIPPED.
     *
     * @param orderId The order ID.
     * @return True if the order was successfully marked as shipped.
     * @throws ValidationException If orderId is null/empty, order not found, or not in processing.
     * @throws NotFoundException If the order is not found.
     */
    public boolean shipOrder(String orderId) {
        Optional<Order> orderOpt = findOrderById(orderId);
        if (orderOpt.isEmpty()) {
            throw new NotFoundException("Order with ID " + orderId + " not found.");
        }
        if (!isProcessing(orderOpt.get())) {
            throw new ValidationException("Only processing orders can be shipped.");
        }
        return updateOrderStatus(orderId, OrderStatus.SHIPPED);
    }

    /**
     * Marks an order as delivered by setting its status to DELIVERED.
     *
     * @param orderId The order ID.
     * @return True if the order was successfully marked as delivered.
     * @throws ValidationException If orderId is null/empty, order not found, or not shipped.
     * @throws NotFoundException If the order is not found.
     */
    public boolean deliverOrder(String orderId) {
        Optional<Order> orderOpt = findOrderById(orderId);
        if (orderOpt.isEmpty()) {
            throw new NotFoundException("Order with ID " + orderId + " not found.");
        }
        if (!isShipped(orderOpt.get())) {
            throw new ValidationException("Only shipped orders can be delivered.");
        }
        return updateOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    /**
     * Returns all orders.
     *
     * @return An unmodifiable list of all orders.
     */
    public List<Order> getAllOrders() {
        return Collections.unmodifiableList(new ArrayList<>(orderList));
    }

    /**
     * Returns orders placed within a specific date range.
     *
     * @param startDate The start date (inclusive).
     * @param endDate   The end date (inclusive).
     * @return An unmodifiable list of orders within the date range.
     * @throws ValidationException If startDate or endDate is null, or startDate is after endDate.
     */
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Objects.requireNonNull(startDate, "Start date cannot be null.");
        Objects.requireNonNull(endDate, "End date cannot be null.");

        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date cannot be after end date.");
        }

        return orderList.stream()
                .filter(order -> !order.getOrderDate().isBefore(startDate) &&
                        !order.getOrderDate().isAfter(endDate))
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns orders with total amount within a specific range.
     *
     * @param minAmount The minimum amount (inclusive).
     * @param maxAmount The maximum amount (inclusive).
     * @return An unmodifiable list of orders within the amount range.
     * @throws ValidationException If amounts are invalid.
     */
    public List<Order> getOrdersByAmountRange(double minAmount, double maxAmount) {
        if (minAmount < 0) {
            throw new ValidationException("Minimum amount cannot be negative.");
        }
        if (maxAmount < minAmount) {
            throw new ValidationException("Maximum amount cannot be less than minimum amount.");
        }

        return orderList.stream()
                .filter(order -> order.getTotalAmount() >= minAmount &&
                        order.getTotalAmount() <= maxAmount)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns orders that are overdue (past expected delivery date and not delivered/cancelled).
     *
     * @return An unmodifiable list of overdue orders.
     */
    public List<Order> getOverdueOrders() {
        return orderList.stream()
                .filter(this::isOverdue)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns orders that contain organic certified products.
     *
     * @return An unmodifiable list of organic certified orders.
     */
    public List<Order> getOrganicCertifiedOrders() {
        return orderList.stream()
                .filter(Order::isOrganicCertified)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns the count of orders by status.
     *
     * @return A map containing the count of orders for each status.
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
     * @return Total revenue from delivered orders.
     */
    public double getTotalRevenue() {
        return orderList.stream()
                .filter(this::isDelivered)
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }

    /**
     * Calculates the total revenue for a specific seller from delivered orders.
     *
     * @param sellerId The seller ID.
     * @return Total revenue for the seller.
     * @throws ValidationException If sellerId is null or empty.
     */
    public double getTotalRevenueForSeller(String sellerId) {
        validateSellerId(sellerId);
        return orderList.stream()
                .filter(order -> order.getSellerId().equals(sellerId) && isDelivered(order))
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }

    /**
     * Returns the average order value.
     *
     * @return Average order value, or 0 if no orders.
     */
    public double getAverageOrderValue() {
        return orderList.stream()
                .mapToDouble(Order::getTotalAmount)
                .average()
                .orElse(0.0);
    }

    /**
     * Checks if the order is pending.
     *
     * @param order The order to check.
     * @return True if status is PENDING.
     */
    public boolean isPending(Order order) {
        return order.getStatus() == OrderStatus.PENDING;
    }

    /**
     * Checks if the order is confirmed.
     *
     * @param order The order to check.
     * @return True if status is CONFIRMED.
     */
    public boolean isConfirmed(Order order) {
        return order.getStatus() == OrderStatus.CONFIRMED;
    }

    /**
     * Checks if the order is in processing.
     *
     * @param order The order to check.
     * @return True if status is PROCESSING.
     */
    public boolean isProcessing(Order order) {
        return order.getStatus() == OrderStatus.PROCESSING;
    }

    /**
     * Checks if the order is shipped.
     *
     * @param order The order to check.
     * @return True if status is SHIPPED.
     */
    public boolean isShipped(Order order) {
        return order.getStatus() == OrderStatus.SHIPPED;
    }

    /**
     * Checks if the order is delivered.
     *
     * @param order The order to check.
     * @return True if status is DELIVERED.
     */
    public boolean isDelivered(Order order) {
        return order.getStatus() == OrderStatus.DELIVERED;
    }

    /**
     * Checks if the order is cancelled.
     *
     * @param order The order to check.
     * @return True if status is CANCELLED.
     */
    public boolean isCancelled(Order order) {
        return order.getStatus() == OrderStatus.CANCELLED;
    }

    /**
     * Checks if the order is overdue (past expected delivery date).
     *
     * @param order The order to check.
     * @return True if overdue, false otherwise.
     */
    public boolean isOverdue(Order order) {
        return order.getExpectedDeliveryDate() != null &&
                !isDelivered(order) &&
                !isCancelled(order) &&
                LocalDateTime.now().isAfter(order.getExpectedDeliveryDate());
    }

    /**
     * Calculates the total amount based on order items.
     *
     * @param orderItems The list of order items.
     * @return The calculated total amount.
     */
    private double calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    /**
     * Validates data required for order creation.
     *
     * @param buyerId    Buyer identifier.
     * @param sellerId   Seller identifier.
     * @param orderItems List of ordered items.
     * @throws ValidationException If any validation fails.
     */
    private void validateOrderCreationData(String buyerId, String sellerId, List<OrderItem> orderItems) {
        validateBuyerId(buyerId);
        validateSellerId(sellerId);
        if (orderItems == null || orderItems.isEmpty()) {
            throw new ValidationException("Order must contain at least one item.");
        }
        for (OrderItem item : orderItems) {
            if (item == null || item.getProductId() == null || item.getProductId().trim().isEmpty() ||
                    item.getQuantity() <= 0 || item.getUnitPrice() < 0) {
                throw new ValidationException("Invalid order item details.");
            }
        }
    }

    /**
     * Validates that the order ID is not null or empty.
     *
     * @param id The identifier to validate.
     * @throws ValidationException If the ID is null or empty.
     */
    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("Order ID cannot be null or empty.");
        }
    }

    /**
     * Validates that the buyer ID is not null or empty.
     *
     * @param buyerId The buyer ID to validate.
     * @throws ValidationException If the buyer ID is null or empty.
     */
    private void validateBuyerId(String buyerId) {
        if (buyerId == null || buyerId.trim().isEmpty()) {
            throw new ValidationException("Buyer ID cannot be null or empty.");
        }
    }

    /**
     * Validates that the seller ID is not null or empty.
     *
     * @param sellerId The seller ID to validate.
     * @throws ValidationException If the seller ID is null or empty.
     */
    private void validateSellerId(String sellerId) {
        if (sellerId == null || sellerId.trim().isEmpty()) {
            throw new ValidationException("Seller ID cannot be null or empty.");
        }
    }

    /**
     * Adds an order to all relevant indexes.
     *
     * @param order The order to add to indexes.
     */
    private void addOrderToIndexes(Order order) {
        orderList.add(order);
        ordersByStatus.computeIfAbsent(order.getStatus(), k -> new ArrayList<>()).add(order);
        ordersByBuyer.computeIfAbsent(order.getBuyerId(), k -> new ArrayList<>()).add(order);
        ordersBySeller.computeIfAbsent(order.getSellerId(), k -> new ArrayList<>()).add(order);
    }

    /**
     * Removes an order from all relevant indexes.
     *
     * @param order The order to remove from indexes.
     */
    private void removeOrderFromIndexes(Order order) {
        ordersByStatus.get(order.getStatus()).remove(order);
        ordersByBuyer.get(order.getBuyerId()).remove(order);
        ordersBySeller.get(order.getSellerId()).remove(order);
    }

    /**
     * Finds the index of an order in the main list by ID.
     *
     * @param orderId The order ID to search for.
     * @return An Optional containing the index or empty if not found.
     */
    private Optional<Integer> findOrderIndex(String orderId) {
        return IntStream.range(0, orderList.size())
                .filter(i -> orderList.get(i).getId().equals(orderId))
                .boxed()
                .findFirst();
    }
}
