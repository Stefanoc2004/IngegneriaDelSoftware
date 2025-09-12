package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a package (bundle) of products in the agricultural supply chain.
 *
 * <p>Implements {@link Prototype} and supports defensive copying of contained products.</p>
 */
@Entity
public class ProductPackage {

    @Id
    private String id;
    private String name;
    @ManyToMany
    private List<Product> products;

    public ProductPackage() {
    }

    public ProductPackage(String id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


    @Override
    public String toString() {
        return "ProductPackage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", products=" + products +
                '}';
    }
}