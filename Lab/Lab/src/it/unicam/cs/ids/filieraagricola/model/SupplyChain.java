package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents an agricultural supply chain (filiera) within the platform.
 * This immutable record encapsulates the supply chain information including
 * associated products, actors, and traceability data.
 *
 * @param id unique identifier for the supply chain
 * @param name the name of the supply chain
 * @param description detailed description of the supply chain
 * @param products list of products associated with this supply chain
 * @param creationDate when this supply chain was created
 * @param territorialArea the geographical area covered by this supply chain
 *
 * @author Agricultural Platform Team
 * @version 1.0
 */
public record SupplyChain(
        String id,
        String name,
        String description,
        List<Product> products,
        LocalDateTime creationDate,
        String territorialArea
) {

    /**
     * Compact constructor for validation and immutability.
     * Ensures all required fields are valid and creates defensive copies.
     */
    public SupplyChain {
        validateId(id);
        validateName(name);
        validateDescription(description);
        validateProducts(products);
        validateCreationDate(creationDate);
        validateTerritorialArea(territorialArea);

        // Create defensive copy of mutable list
        products = products == null ? List.of() : List.copyOf(products);

        // Normalize strings
        name = name.trim();
        description = description.trim();
        territorialArea = territorialArea.trim();
    }

    /**
     * Constructor with minimal parameters - used by SupplyChainService
     */
    public SupplyChain(String name, List<Product> products) {
        this(
                generateId(name, "default"),
                name,
                "Supply chain for " + name,
                products,
                LocalDateTime.now(),
                "default"
        );
    }

    /**
     * Creates a new SupplyChain with validated parameters.
     * Factory method that ensures proper object construction.
     *
     * @param name the supply chain name
     * @param description supply chain description
     * @param products list of associated products
     * @param territorialArea geographical area
     * @return a new validated SupplyChain instance
     */
    public static SupplyChain create(String name, String description,
                                     List<Product> products, String territorialArea) {
        var id = generateId(name, territorialArea);
        var creationDate = LocalDateTime.now();
        return new SupplyChain(id, name, description, products, creationDate, territorialArea);
    }

    /**
     * Gets the name of the supply chain.
     * @return the supply chain name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of products in this supply chain.
     *
     * @return the count of products
     */
    public int getProductCount() {
        return products.size();
    }

    /**
     * Checks if this supply chain contains a specific product.
     *
     * @param product the product to search for
     * @return true if the product is found
     */
    public boolean containsProduct(Product product) {
        return products.contains(product);
    }

    /**
     * Gets all organic products in this supply chain.
     *
     * @return a list of organic products
     */
    public List<Product> getOrganicProducts() {
        return products.stream()
                .filter(Product::isOrganic)
                .toList();
    }

    /**
     * Gets all certified products in this supply chain.
     *
     * @return a list of certified products
     */
    public List<Product> getCertifiedProducts() {
        return products.stream()
                .filter(Product::hasCertifications)
                .toList();
    }

    /**
     * Gets products by category.
     *
     * @param category the category to filter by
     * @return a list of products in the specified category
     */
    public List<Product> getProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return List.of();
        }

        return products.stream()
                .filter(product -> product.category().equalsIgnoreCase(category.trim()))
                .toList();
    }

    /**
     * Gets fresh products (produced within last 30 days).
     *
     * @return a list of fresh products
     */
    public List<Product> getFreshProducts() {
        return products.stream()
                .filter(Product::isFresh)
                .toList();
    }

    /**
     * Checks if this supply chain is active (has products and recent activity).
     *
     * @return true if the supply chain is considered active
     */
    public boolean isActive() {
        return !products.isEmpty() &&
                creationDate.isAfter(LocalDateTime.now().minusMonths(6));
    }

    /**
     * Gets unique categories of products in this supply chain.
     *
     * @return a set of unique categories
     */
    public Set<String> getUniqueCategories() {
        return products.stream()
                .map(Product::category)
                .collect(Collectors.toSet());
    }

    /**
     * Validates the supply chain ID is not null or empty.
     */
    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Supply chain ID cannot be null or empty");
        }
    }

    /**
     * Validates the supply chain name is not null or empty.
     */
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Supply chain name cannot be null or empty");
        }
    }

    /**
     * Validates the supply chain description is not null or empty.
     */
    private static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Supply chain description cannot be null or empty");
        }
    }

    /**
     * Validates the products list is not null.
     */
    private static void validateProducts(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("Products list cannot be null");
        }
    }

    /**
     * Validates the creation date is not null.
     */
    private static void validateCreationDate(LocalDateTime creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException("Creation date cannot be null");
        }
    }

    /**
     * Validates the territorial area is not null or empty.
     */
    private static void validateTerritorialArea(String area) {
        if (area == null || area.trim().isEmpty()) {
            throw new IllegalArgumentException("Territorial area cannot be null or empty");
        }
    }

    /**
     * Generates a unique ID based on name and territorial area.
     */
    private static String generateId(String name, String area) {
        return (name + "_" + area + "_" + System.currentTimeMillis())
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }
}
