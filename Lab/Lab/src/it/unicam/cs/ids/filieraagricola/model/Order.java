package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an order in the agricultural supply chain platform.
 *
 * <p>An Order contains information about a purchase transaction, including
 * the buyer, ordered items (products or packages), quantities, prices,
 * delivery information, and order status. Orders track the complete
 * lifecycle from creation to delivery completion.</p>
 *
 * <p>This class implements the Prototype pattern to enable defensive copying
 * and safe object management throughout the system.</p>
 */
public class Order implements Prototype<Order> {

    /**
     * Unique identifier for the order. Automatically generated if not provided.
     */
    private String id;

    /**
     * ID of the buyer who placed the order
     */
    private String buyerId;

    /**
     * ID of the seller (producer, transformer, or distributor)
     */
    private String sellerId;

    /**
     * List of ordered items with quantities and prices
     */
    private List<OrderItem> orderItems;

    /**
     * Total amount of the order
     */
    private double totalAmount;

    /**
     * Current status of the order
     */
    private OrderStatus status;

    /**
     * Date and time when the order was created
     */
    private LocalDateTime orderDate;

    /**
     * Expected delivery date
     */
    private LocalDateTime expectedDeliveryDate;

    /**
     * Actual delivery date (null until delivered)
     */
    private LocalDateTime actualDeliveryDate;

    /**
     * Delivery address for the order
     */
    private String deliveryAddress;

    /**
     * Payment method used for the order
     */
    private String paymentMethod;

    /**
     * Additional notes or special instructions
     */
    private String notes;

    /**
     * Whether the order qualifies for organic certification
     */
    private boolean organicCertified;

    /**
     * Delivery method (e.g., "home_delivery", "pickup", "courier")
     */
    private String deliveryMethod;

    /**
     * Default constructor for frameworks and prototype pattern.
     * Generates a unique ID and sets default values.
     */
    public Order() {
        this.id = UUID.randomUUID().toString();
        this.orderItems = new ArrayList<>();
        this.totalAmount = 0.0;
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
        this.organicCertified = false;
    }

    /**
     * Full constructor with validation and normalization.
     *
     * @param id unique identifier (if null or empty, a new UUID will be generated)
     * @param buyerId buyer identifier (must not be null or empty)
     * @param sellerId seller identifier (must not be null or empty)
     * @param orderItems list of ordered items (may be null, will be initialized as empty)
     * @param totalAmount total order amount (must be >= 0)
     * @param status order status (must not be null)
     * @param orderDate order creation date (must not be null)
     * @param expectedDeliveryDate expected delivery date (may be null)
     * @param deliveryAddress delivery address (may be null)
     * @param paymentMethod payment method (may be null)
     * @param deliveryMethod delivery method (may be null)
     * @throws IllegalArgumentException if any validation fails
     */
    public Order(String id, String buyerId, String sellerId, List<OrderItem> orderItems,
                 double totalAmount, OrderStatus status, LocalDateTime orderDate,
                 LocalDateTime expectedDeliveryDate, String deliveryAddress,
                 String paymentMethod, String deliveryMethod) {
        this.id = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id.trim();

        validateBuyerId(buyerId);
        validateSellerId(sellerId);
        validateTotalAmount(totalAmount);
        validateStatus(status);
        validateOrderDate(orderDate);

        this.buyerId = buyerId.trim();
        this.sellerId = sellerId.trim();
        this.orderItems = orderItems != null ? new ArrayList<>(orderItems) : new ArrayList<>();
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.actualDeliveryDate = null;
        this.deliveryAddress = deliveryAddress != null ? deliveryAddress.trim() : null;
        this.paymentMethod = paymentMethod != null ? paymentMethod.trim() : null;
        this.deliveryMethod = deliveryMethod != null ? deliveryMethod.trim() : null;
        this.notes = null;
        this.organicCertified = false;
    }

    /**
     * Copy constructor for cloning.
     *
     * @param other the Order instance to copy
     * @throws IllegalArgumentException if other is null
     */
    public Order(Order other) {
        Objects.requireNonNull(other, "Order to copy cannot be null");
        this.id = other.id;
        this.buyerId = other.buyerId;
        this.sellerId = other.sellerId;
        this.orderItems = new ArrayList<>(other.orderItems);
        this.totalAmount = other.totalAmount;
        this.status = other.status;
        this.orderDate = other.orderDate;
        this.expectedDeliveryDate = other.expectedDeliveryDate;
        this.actualDeliveryDate = other.actualDeliveryDate;
        this.deliveryAddress = other.deliveryAddress;
        this.paymentMethod = other.paymentMethod;
        this.notes = other.notes;
        this.organicCertified = other.organicCertified;
        this.deliveryMethod = other.deliveryMethod;
    }

    /**
     * Creates a deep copy of this Order instance.
     * This method implements the Prototype pattern.
     *
     * @return a new Order instance that is a copy of this instance
     */
    @Override
    public Order clone() {
        return new Order(this);
    }

    /**
     * Returns the unique identifier of this order.
     *
     * @return the order ID, never null after construction
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this order.
     *
     * @param id the order identifier to set
     * @throws IllegalArgumentException if id is null or empty after trimming
     */
    public void setId(String id) {
        validateId(id);
        this.id = id.trim();
    }

    /**
     * Returns the buyer ID.
     *
     * @return the buyer ID
     */
    public String getBuyerId() {
        return buyerId;
    }

    /**
     * Sets the buyer ID.
     *
     * @param buyerId the buyer ID to set
     * @throws IllegalArgumentException if buyerId is null or empty
     */
    public void setBuyerId(String buyerId) {
        validateBuyerId(buyerId);
        this.buyerId = buyerId.trim();
    }

    /**
     * Returns the seller ID.
     *
     * @return the seller ID
     */
    public String getSellerId() {
        return sellerId;
    }

    /**
     * Sets the seller ID.
     *
     * @param sellerId the seller ID to set
     * @throws IllegalArgumentException if sellerId is null or empty
     */
    public void setSellerId(String sellerId) {
        validateSellerId(sellerId);
        this.sellerId = sellerId.trim();
    }

    /**
     * Returns the list of order items.
     *
     * @return defensive copy of order items list
     */
    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    /**
     * Sets the order items for this order.
     *
     * @param orderItems list of order items (may be null, will be treated as empty)
     */
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems != null ? new ArrayList<>(orderItems) : new ArrayList<>();
        recalculateTotalAmount();
    }

    /**
     * Adds an order item to this order.
     *
     * @param orderItem order item to add (must not be null)
     * @throws IllegalArgumentException if orderItem is null
     */
    public void addOrderItem(OrderItem orderItem) {
        if (orderItem == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }
        orderItems.add(orderItem);
        recalculateTotalAmount();
    }

    /**
     * Removes an order item from this order.
     *
     * @param orderItem order item to remove
     * @return true if the item was removed, false if it wasn't found
     */
    public boolean removeOrderItem(OrderItem orderItem) {
        boolean removed = orderItems.remove(orderItem);
        if (removed) {
            recalculateTotalAmount();
        }
        return removed;
    }

    /**
     * Returns the total amount of the order.
     *
     * @return total amount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the total amount of the order.
     *
     * @param totalAmount total amount (must be >= 0)
     * @throws IllegalArgumentException if totalAmount is negative
     */
    public void setTotalAmount(double totalAmount) {
        validateTotalAmount(totalAmount);
        this.totalAmount = totalAmount;
    }

    /**
     * Returns the order status.
     *
     * @return order status
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Sets the order status.
     *
     * @param status order status (must not be null)
     * @throws IllegalArgumentException if status is null
     */
    public void setStatus(OrderStatus status) {
        validateStatus(status);
        this.status = status;

        // Automatically set delivery date when status changes to DELIVERED
        if (status == OrderStatus.DELIVERED && actualDeliveryDate == null) {
            actualDeliveryDate = LocalDateTime.now();
        }
    }

    /**
     * Returns the order creation date.
     *
     * @return order date
     */
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the order creation date.
     *
     * @param orderDate order date (must not be null)
     * @throws IllegalArgumentException if orderDate is null
     */
    public void setOrderDate(LocalDateTime orderDate) {
        validateOrderDate(orderDate);
        this.orderDate = orderDate;
    }

    /**
     * Returns the expected delivery date.
     *
     * @return expected delivery date, may be null
     */
    public LocalDateTime getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    /**
     * Sets the expected delivery date.
     *
     * @param expectedDeliveryDate expected delivery date (may be null)
     */
    public void setExpectedDeliveryDate(LocalDateTime expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    /**
     * Returns the actual delivery date.
     *
     * @return actual delivery date, null if not yet delivered
     */
    public LocalDateTime getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    /**
     * Sets the actual delivery date.
     *
     * @param actualDeliveryDate actual delivery date (may be null)
     */
    public void setActualDeliveryDate(LocalDateTime actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    /**
     * Returns the delivery address.
     *
     * @return delivery address, may be null
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Sets the delivery address.
     *
     * @param deliveryAddress delivery address (may be null)
     */
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress != null ? deliveryAddress.trim() : null;
    }

    /**
     * Returns the payment method.
     *
     * @return payment method, may be null
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Sets the payment method.
     *
     * @param paymentMethod payment method (may be null)
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod != null ? paymentMethod.trim() : null;
    }

    /**
     * Returns the order notes.
     *
     * @return order notes, may be null
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the order notes.
     *
     * @param notes order notes (may be null)
     */
    public void setNotes(String notes) {
        this.notes = notes != null ? notes.trim() : null;
    }

    /**
     * Returns whether the order is organic certified.
     *
     * @return true if organic certified
     */
    public boolean isOrganicCertified() {
        return organicCertified;
    }

    /**
     * Sets the organic certification status.
     *
     * @param organicCertified organic certification status
     */
    public void setOrganicCertified(boolean organicCertified) {
        this.organicCertified = organicCertified;
    }

    /**
     * Returns the delivery method.
     *
     * @return delivery method, may be null
     */
    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    /**
     * Sets the delivery method.
     *
     * @param deliveryMethod delivery method (may be null)
     */
    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod != null ? deliveryMethod.trim() : null;
    }

    /**
     * Checks if the order is pending.
     *
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return status == OrderStatus.PENDING;
    }

    /**
     * Checks if the order is confirmed.
     *
     * @return true if status is CONFIRMED
     */
    public boolean isConfirmed() {
        return status == OrderStatus.CONFIRMED;
    }

    /**
     * Checks if the order is in processing.
     *
     * @return true if status is PROCESSING
     */
    public boolean isProcessing() {
        return status == OrderStatus.PROCESSING;
    }

    /**
     * Checks if the order is shipped.
     *
     * @return true if status is SHIPPED
     */
    public boolean isShipped() {
        return status == OrderStatus.SHIPPED;
    }

    /**
     * Checks if the order is delivered.
     *
     * @return true if status is DELIVERED
     */
    public boolean isDelivered() {
        return status == OrderStatus.DELIVERED;
    }

    /**
     * Checks if the order is cancelled.
     *
     * @return true if status is CANCELLED
     */
    public boolean isCancelled() {
        return status == OrderStatus.CANCELLED;
    }

    /**
     * Returns the number of items in the order.
     *
     * @return number of order items
     */
    public int getItemCount() {
        return orderItems.size();
    }

    /**
     * Returns the total quantity of all items in the order.
     *
     * @return total quantity
     */
    public int getTotalQuantity() {
        return orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    /**
     * Checks if the order is overdue (past expected delivery date).
     *
     * @return true if overdue, false otherwise
     */
    public boolean isOverdue() {
        return expectedDeliveryDate != null &&
                !isDelivered() &&
                !isCancelled() &&
                LocalDateTime.now().isAfter(expectedDeliveryDate);
    }

    /**
     * Recalculates the total amount based on order items.
     */
    private void recalculateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }

    // ----------------- validation helpers -----------------

    /**
     * Validates that the order ID is not null or empty.
     *
     * @param id the identifier to validate
     * @throws IllegalArgumentException if the ID is null or empty
     */
    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
    }

    /**
     * Validates that the buyer ID is not null or empty.
     *
     * @param buyerId the buyer ID to validate
     * @throws IllegalArgumentException if the buyer ID is null or empty
     */
    private static void validateBuyerId(String buyerId) {
        if (buyerId == null || buyerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Buyer ID cannot be null or empty");
        }
    }

    /**
     * Validates that the seller ID is not null or empty.
     *
     * @param sellerId the seller ID to validate
     * @throws IllegalArgumentException if the seller ID is null or empty
     */
    private static void validateSellerId(String sellerId) {
        if (sellerId == null || sellerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Seller ID cannot be null or empty");
        }
    }

    /**
     * Validates that the total amount is not negative.
     *
     * @param totalAmount the total amount to validate
     * @throws IllegalArgumentException if the total amount is negative
     */
    private static void validateTotalAmount(double totalAmount) {
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative");
        }
    }

    /**
     * Validates that the order status is not null.
     *
     * @param status the status to validate
     * @throws IllegalArgumentException if the status is null
     */
    private static void validateStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Order status cannot be null");
        }
    }

    /**
     * Validates that the order date is not null.
     *
     * @param orderDate the order date to validate
     * @throws IllegalArgumentException if the order date is null
     */
    private static void validateOrderDate(LocalDateTime orderDate) {
        if (orderDate == null) {
            throw new IllegalArgumentException("Order date cannot be null");
        }
    }

    // ----------------- equals/hashCode/toString -----------------

    /**
     * Indicates whether some other object is "equal to" this Order.
     * Two Order objects are considered equal if they have the same ID.
     *
     * @param o the reference object with which to compare
     * @return true if this object is equal to the o argument; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    /**
     * Returns a hash code value for this Order.
     * The hash code is based on the order ID.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a string representation of this Order.
     * The string includes all major fields for debugging purposes.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", buyerId='" + buyerId + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", orderDate=" + orderDate +
                ", itemCount=" + orderItems.size() +
                ", organicCertified=" + organicCertified +
                '}';
    }

    /**
     * Represents an item within an order.
     */
    public static class OrderItem {
        private String productId;
        private String productName;
        private int quantity;
        private double unitPrice;
        private String notes;

        /**
         * Default constructor for OrderItem.
         */
        public OrderItem() {
            this.quantity = 1;
            this.unitPrice = 0.0;
        }

        /**
         * Full constructor for OrderItem.
         *
         * @param productId product identifier
         * @param productName product name
         * @param quantity quantity ordered
         * @param unitPrice price per unit
         * @param notes additional notes
         */
        public OrderItem(String productId, String productName, int quantity, double unitPrice, String notes) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.notes = notes;
        }

        // Getters and setters
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }

        public double getTotalPrice() {
            return quantity * unitPrice;
        }

        @Override
        public String toString() {
            return "OrderItem{" +
                    "productId='" + productId + '\'' +
                    ", productName='" + productName + '\'' +
                    ", quantity=" + quantity +
                    ", unitPrice=" + unitPrice +
                    ", totalPrice=" + getTotalPrice() +
                    '}';
        }
    }
}
