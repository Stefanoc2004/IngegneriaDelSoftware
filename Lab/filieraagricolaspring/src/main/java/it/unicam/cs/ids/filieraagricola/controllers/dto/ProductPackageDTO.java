package it.unicam.cs.ids.filieraagricola.controllers.dto;

import java.util.List;

public class ProductPackageDTO {

    private String name;
    private List<String> productsId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getProductsId() {
        return productsId;
    }

    public void setProductsId(List<String> productsId) {
        this.productsId = productsId;
    }
}
