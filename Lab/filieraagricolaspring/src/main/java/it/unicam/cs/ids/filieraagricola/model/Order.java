package it.unicam.cs.ids.filieraagricola.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.sql.Timestamp;
import java.util.List;
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
@Entity
public class Order  {

    /**
     * Unique identifier for the order. Automatically generated if not provided.
     */
    @Id
    private String id;

    /**
     * ID of the buyer who placed the order
     */
    @ManyToOne
    private User buyer;

    /**
     * ID of the seller (producer, transformer, or distributor)
     */
    @ManyToOne
    private User seller;

    /**
     * List of ordered items with quantities and prices
     */
    @OneToMany
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
    private Timestamp orderDate;

    /**
     * Expected delivery date
     */
    private Timestamp expectedDeliveryDate;

    /**
     * Actual delivery date (null until delivered)
     */
    private Timestamp actualDeliveryDate;

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
    @Nullable
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
    }

    public Order(String id, User buyer, User seller, List<OrderItem> orderItems, double totalAmount, OrderStatus status, Timestamp orderDate, Timestamp expectedDeliveryDate, Timestamp actualDeliveryDate, String deliveryAddress, String paymentMethod, String notes, boolean organicCertified, String deliveryMethod) {
        this.id = id;
        this.buyer = buyer;
        this.seller = seller;
        this.orderItems = orderItems;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public Timestamp getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Timestamp expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public Timestamp getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    public void setActualDeliveryDate(Timestamp actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isOrganicCertified() {
        return organicCertified;
    }

    public void setOrganicCertified(boolean organicCertified) {
        this.organicCertified = organicCertified;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", buyer=" + buyer +
                ", seller=" + seller +
                ", orderItems=" + orderItems +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", orderDate=" + orderDate +
                ", expectedDeliveryDate=" + expectedDeliveryDate +
                ", actualDeliveryDate=" + actualDeliveryDate +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", notes='" + notes + '\'' +
                ", organicCertified=" + organicCertified +
                ", deliveryMethod='" + deliveryMethod + '\'' +
                '}';
    }
}

