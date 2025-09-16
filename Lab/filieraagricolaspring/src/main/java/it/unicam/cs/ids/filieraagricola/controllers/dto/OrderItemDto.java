package it.unicam.cs.ids.filieraagricola.controllers.dto;

import it.unicam.cs.ids.filieraagricola.model.Product;

public class OrderItemDto {

    private String productId;

    private int quantity;

    private String notes;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
