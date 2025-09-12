package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderItem {
        @Id
        private String id;
        @ManyToOne
        private Product product;
        private int quantity;
        private String notes;


    public OrderItem() {
    }

    public OrderItem(String id, Product product, int quantity, String notes) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
