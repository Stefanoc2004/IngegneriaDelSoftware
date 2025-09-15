package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an order in the agricultural supply chain platform.
 *
 * <p>An Order contains information about a purchase transaction, including
 * the buyer, ordered items (products or packages), quantities, prices,
 * delivery information, and order status.
 * This class is a pure data model and does not contain business logic for validation,
 * state transitions, or complex calculations.
 * Business logic, such as order creation, status updates, and total amount calculation,
 * is handled by {@link it.unicam.cs.ids.filieraagricola.service.OrderService}.</p>
 */
public class Order {

    /**
     * Unique identifier for the order.
     */
    private final String id;

    /**
     * ID of the buyer who placed the order.
     */
    private final String buyerId;

    /**
     * ID of the seller (producer, transformer, or distributor).
     */
    private final String sellerId;

    /**
     * List of ordered items with quantities and prices.
     */
    private final List<OrderItem> orderItems;

    /**
     * Total amount of the order.
     */
    private final double totalAmount;

    /**
     * Current status of the order.
     */
    private final OrderStatus status;

    /**
     * Date and time when the order was created.
     */
    private final LocalDateTime orderDate;

    /**
     * Expected delivery date.
     */
    private final LocalDateTime expectedDeliveryDate;

    /**
     * Actual delivery date (null until delivered).
     */
    private final LocalDateTime actualDeliveryDate;

    /**
     * Delivery address for the order.
     */
    private final String deliveryAddress;

    /**
     * Payment method used for the order.
     */
    private final String paymentMethod;

    /**
     * Additional notes or special instructions.
     */
    private final String notes;

    /**
     * Whether the order qualifies for organic certification.
     */
    private final boolean organicCertified;

    /**
     * Delivery method (e.g., "home_delivery", "pickup", "courier").
     */
    private final String deliveryMethod;

    /**
     * Constructs a new Order instance with a generated ID.
     * This constructor is primarily for internal use by services when creating new orders.
     *
     * @param buyerId            Buyer identifier.
     * @param sellerId           Seller identifier.
     * @param orderItems         List of ordered items.
     * @param totalAmount        Total amount of the order.
     * @param status             Order status.
     * @param orderDate          Order creation date.
     * @param expectedDeliveryDate Expected delivery date.
     * @param actualDeliveryDate Actual delivery date.
     * @param deliveryAddress    Delivery address.
     * @param paymentMethod      Payment method.
     * @param notes              Additional notes.
     * @param organicCertified   Whether the order qualifies for organic certification.
     * @param deliveryMethod     Delivery method.
     */
    public Order(String buyerId, String sellerId, List<OrderItem> orderItems,
                 double totalAmount, OrderStatus status, LocalDateTime orderDate,
                 LocalDateTime expectedDeliveryDate, LocalDateTime actualDeliveryDate,
                 String deliveryAddress, String paymentMethod, String notes,
                 boolean organicCertified, String deliveryMethod) {
        this.id = UUID.randomUUID().toString();
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.orderItems = Collections.unmodifiableList(new ArrayList<>(orderItems));
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.actualDeliveryDate = actualDeliveryDate;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.organicCertified = organicCertified;
        this.deliveryMethod = deliveryMethod;
    }

    /**
     * Constructs a new Order instance with a specified ID.
     * This constructor is typically used when reconstructing an Order object from persistence
     * or when a specific ID is required.
     *
     * @param id                 Unique identifier for the order.
     * @param buyerId            Buyer identifier.
     * @param sellerId           Seller identifier.
     * @param orderItems         List of ordered items.
     * @param totalAmount        Total amount of the order.
     * @param status             Order status.
     * @param orderDate          Order creation date.
     * @param expectedDeliveryDate Expected delivery date.
     * @param actualDeliveryDate Actual delivery date.
     * @param deliveryAddress    Delivery address.
     * @param paymentMethod      Payment method.
     * @param notes              Additional notes.
     * @param organicCertified   Whether the order qualifies for organic certification.
     * @param deliveryMethod     Delivery method.
     */
    public Order(String id, String buyerId, String sellerId, List<OrderItem> orderItems,
                 double totalAmount, OrderStatus status, LocalDateTime orderDate,
                 LocalDateTime expectedDeliveryDate, LocalDateTime actualDeliveryDate,
                 String deliveryAddress, String paymentMethod, String notes,
                 boolean organicCertified, String deliveryMethod) {
        this.id = id;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.orderItems = Collections.unmodifiableList(new ArrayList<>(orderItems));
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.actualDeliveryDate = actualDeliveryDate;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.organicCertified = organicCertified;
        this.deliveryMethod = deliveryMethod;
    }

    /**
     * Returns the unique identifier of this order.
     *
     * @return The order ID, never null after construction.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the buyer ID.
     *
     * @return The buyer ID.
     */
    public String getBuyerId() {
        return buyerId;
    }

    /**
     * Returns the seller ID.
     *
     * @return The seller ID.
     */
    public String getSellerId() {
        return sellerId;
    }

    /**
     * Returns the list of order items.
     *
     * @return An unmodifiable list of order items.
     */
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    /**
     * Returns the total amount of the order.
     *
     * @return Total amount.
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Returns the order status.
     *
     * @return Order status.
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Returns the order creation date.
     *
     * @return Order date.
     */
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    /**
     * Returns the expected delivery date.
     *
     * @return Expected delivery date, may be null.
     */
    public LocalDateTime getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    /**
     * Returns the actual delivery date.
     *
     * @return Actual delivery date, null if not yet delivered.
     */
    public LocalDateTime getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    /**
     * Returns the delivery address.
     *
     * @return Delivery address, may be null.
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Returns the payment method.
     *
     * @return Payment method, may be null.
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Returns the order notes.
     *
     * @return Order notes, may be null.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Returns whether the order is organic certified.
     *
     * @return True if organic certified.
     */
    public boolean isOrganicCertified() {
        return organicCertified;
    }

    /**
     * Returns the delivery method.
     *
     * @return Delivery method, may be null.
     */
    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    /**
     * Indicates whether some other object is "equal to" this Order.
     * Two Order objects are considered equal if they have the same ID.
     *
     * @param o The reference object with which to compare.
     * @return True if this object is equal to the o argument; false otherwise.
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
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a string representation of this Order.
     * The string includes all major fields for debugging purposes.
     *
     * @return A string representation of this object.
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
     * This is a nested static class as it is intrinsically linked to an Order.
     */
    public static class OrderItem {
        private final String productId;
        private final String productName;
        private final int quantity;
        private final double unitPrice;
        private final String notes;

        /**
         * Constructs a new OrderItem.
         *
         * @param productId   Product identifier.
         * @param productName Product name.
         * @param quantity    Quantity ordered.
         * @param unitPrice   Price per unit.
         * @param notes       Additional notes.
         */
        public OrderItem(String productId, String productName, int quantity, double unitPrice, String notes) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.notes = notes;
        }

        /**
         * Returns the product identifier.
         *
         * @return Product ID.
         */
        public String getProductId() {
            return productId;
        }

        /**
         * Returns the product name.
         *
         * @return Product name.
         */
        public String getProductName() {
            return productName;
        }

        /**
         * Returns the quantity ordered.
         *
         * @return Quantity.
         */
        public int getQuantity() {
            return quantity;
        }

        /**
         * Returns the price per unit.
         *
         * @return Unit price.
         */
        public double getUnitPrice() {
            return unitPrice;
        }

        /**
         * Returns additional notes for the item.
         *
         * @return Notes.
         */
        public String getNotes() {
            return notes;
        }

        /**
         * Calculates the total price for this order item.
         *
         * @return Total price (quantity * unit price).
         */
        public double getTotalPrice() {
            return quantity * unitPrice;
        }

        /**
         * Returns a string representation of this OrderItem.
         *
         * @return A string representation of this object.
         */
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

        /**
         * Indicates whether some other object is "equal to" this OrderItem.
         * Two OrderItem objects are considered equal if they have the same product ID.
         *
         * @param o The reference object with which to compare.
         * @return True if this object is equal to the o argument; false otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderItem orderItem = (OrderItem) o;
            return Objects.equals(productId, orderItem.productId);
        }

        /**
         * Returns a hash code value for this OrderItem.
         * The hash code is based on the product ID.
         *
         * @return A hash code value for this object.
         */
        @Override
        public int hashCode() {
            return Objects.hash(productId);
        }
    }
}
