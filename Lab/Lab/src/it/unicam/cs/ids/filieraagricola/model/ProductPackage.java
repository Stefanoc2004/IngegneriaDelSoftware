package it.unicam.cs.ids.filieraagricola.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents a package of products in the agricultural supply chain.
 * This record encapsulates package information including name and associated products.
 *
 * @param name the name of the package
 * @param products the list of products in this package
 */
public record ProductPackage(
        String name,
        List<Product> products
) {

    /**
     * Compact constructor for validation.
     */
    public ProductPackage {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Package name cannot be null or empty");
        }
        if (products == null) {
            throw new IllegalArgumentException("Products list cannot be null");
        }

        // Create defensive copy and normalize
        name = name.trim();
        products = List.copyOf(products);
    }

    /**
     * Gets the name of the package.
     * @return the package name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the products in the package.
     * @return the list of products
     */
    public List<Product> getProducts() {
        return products;
    }
}