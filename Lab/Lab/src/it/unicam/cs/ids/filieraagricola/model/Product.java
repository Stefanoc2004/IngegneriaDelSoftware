package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an agricultural product within the supply chain platform.
 * This immutable record encapsulates all product-related information including
 * cultivation methods, certifications, and quality data.
 *
 * @param id unique identifier for the product
 * @param name the commercial name of the product
 * @param category the category classification (e.g., "vegetables", "fruits", "grains")
 * @param description detailed description of the product
 * @param cultivationMethod the method used for cultivation (e.g., "organic", "traditional")
 * @param certifications quality certifications associated with the product
 * @param productionDate the date when the product was produced/harvested
 * @param producerId identifier of the producer who created this product
 *
 * @author Agricultural Platform Team
 * @version 1.0
 */
public record Product(
        String id,
        String name,
        String category,
        String description,
        String cultivationMethod,
        String certifications,
        LocalDate productionDate,
        String producerId
) {

    /**
     * Compact constructor for validation and normalization.
     * Ensures all required fields are valid according to business rules.
     */
    public Product {
        validateId(id);
        validateName(name);
        validateCategory(category);
        validateDescription(description);
        validateCultivationMethod(cultivationMethod);
        validateProductionDate(productionDate);
        validateProducerId(producerId);

        // Normalize strings
        name = name.trim();
        category = category.toLowerCase().trim();
        description = description.trim();
        cultivationMethod = cultivationMethod.toLowerCase().trim();
    }

    /**
     * Creates a new Product with validated parameters.
     * Factory method that ensures proper object construction.
     *
     * @param name the product name
     * @param category the product category
     * @param description product description
     * @param cultivationMethod cultivation method used
     * @param certifications quality certifications
     * @param productionDate production date
     * @param producerId producer identifier
     * @return a new validated Product instance
     */
    public static Product create(String name, String category, String description,
                                 String cultivationMethod, String certifications,
                                 LocalDate productionDate, String producerId) {
        var id = generateId(name, producerId);
        return new Product(id, name, category, description, cultivationMethod,
                certifications, productionDate, producerId);
    }

    /**
     * Gets the category of the product.
     * @return the product category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Checks if this product is organic based on cultivation method.
     *
     * @return true if the product is organically cultivated
     */
    public boolean isOrganic() {
        return cultivationMethod.contains("organic") ||
                cultivationMethod.contains("biologico");
    }

    /**
     * Checks if the product has quality certifications.
     *
     * @return true if certifications are present and not empty
     */
    public boolean hasCertifications() {
        return certifications != null && !certifications.trim().isEmpty();
    }

    /**
     * Checks if the product is fresh based on production date.
     * A product is considered fresh if produced within the last 30 days.
     *
     * @return true if the product is fresh
     */
    public boolean isFresh() {
        return productionDate.isAfter(LocalDate.now().minusDays(30));
    }

    /**
     * Validates the product ID is not null or empty.
     */
    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
    }

    /**
     * Validates the product name is not null or empty.
     */
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
    }

    /**
     * Validates the product category is not null or empty.
     */
    private static void validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Product category cannot be null or empty");
        }
    }

    /**
     * Validates the product description is not null or empty.
     */
    private static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }
    }

    /**
     * Validates the cultivation method is not null or empty.
     */
    private static void validateCultivationMethod(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Cultivation method cannot be null or empty");
        }
    }

    /**
     * Validates the production date is not null or in the future.
     */
    private static void validateProductionDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Production date cannot be null");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Production date cannot be in the future");
        }
    }

    /**
     * Validates the producer ID is not null or empty.
     */
    private static void validateProducerId(String producerId) {
        if (producerId == null || producerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Producer ID cannot be null or empty");
        }
    }

    /**
     * Generates a unique ID based on product name and producer ID.
     */
    private static String generateId(String name, String producerId) {
        return (name + "_" + producerId + "_" + System.currentTimeMillis()).toLowerCase();
    }
}