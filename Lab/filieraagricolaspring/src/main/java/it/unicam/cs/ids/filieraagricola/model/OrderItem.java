package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Line item within an {@link Order} referencing a {@link Product} and quantity.
 *
 * <p>This entity preserves the original structure and behaviour; JavaDoc was
 * added to clarify intent and usage without altering logic.</p>
 */
@Entity
public class OrderItem {
    @Id
    private String id;
    @ManyToOne
    private Product product;
    private int quantity;
    private String notes;


    /**
     * Default constructor required by JPA and frameworks.
     */
    public OrderItem() {
    }

    /**
     * Full constructor initializing all fields as provided.
     *
     * @param id       line item identifier
     * @param product  referenced product
     * @param quantity ordered quantity
     * @param notes    optional notes
     */
    public OrderItem(String id, Product product, int quantity, String notes) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.notes = notes;
    }

    /** Returns the line item identifier. */
    public String getId() {
        return id;
    }

    /** Sets the line item identifier. */
    public void setId(String id) {
        this.id = id;
    }

    /** Returns the referenced product. */
    public Product getProduct() {
        return product;
    }

    /** Sets the referenced product. */
    public void setProduct(Product product) {
        this.product = product;
    }

    /** Returns the ordered quantity. */
    public int getQuantity() {
        return quantity;
    }

    /** Sets the ordered quantity. */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /** Returns optional notes for this line item. */
    public String getNotes() {
        return notes;
    }

    /** Sets optional notes for this line item. */
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
