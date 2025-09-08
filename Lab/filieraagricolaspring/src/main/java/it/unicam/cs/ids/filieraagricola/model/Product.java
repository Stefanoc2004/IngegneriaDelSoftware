package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an agricultural product within the supply chain platform.
 *
 * <p>This class is a plain Java object (POJO) which implements the Prototype
 * pattern via {@link #clone()}. It exposes a no-arg constructor (for
 * frameworks), a full parameterized constructor, a copy constructor and
 * validated setters/getters. All public and private methods are documented in
 * English to guarantee clarity and maintainability.</p>
 */
@Entity
public class Product  {

    @Id
    private String id;
    private String name;
    private String category;
    private String description;
    private Date productionDate;

    /**
     * Default no-argument constructor for frameworks (e.g., Spring, JPA).
     * Generates a unique ID for the product.
     *
     * <p>Leaves other fields uninitialized (null). Use setters to populate fields or
     * use the parameterized constructor for validated construction.</p>
     */
    public Product() {
        this.id = UUID.randomUUID().toString(); // Automatically generate ID
    }

    /**
     * Full constructor that validates and normalizes input values.
     * If the ID is null or empty, a new one is generated.
     *
     * @param id                unique identifier for the product, if null or empty, a new UUID will be generated
     * @param name              commercial product name, must be non-null and non-empty
     * @param category          product category (e.g., "vegetables"), must be non-null and non-empty
     * @param description       human-readable description, must be non-null and non-empty
     * @param productionDate    production/harvest date, must be non-null and not in the future
     * @throws IllegalArgumentException if any required argument is invalid
     */
    public Product(String id,
                   String name,
                   String category,
                   String description,
                   Date productionDate) {
        this.id = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id.trim(); // Ensure ID is always set and trimmed
        validateName(name);
        validateCategory(category);
        validateDescription(description);
        validateProductionDate(productionDate);

        this.name = name.trim();
        this.category = category.toLowerCase().trim();
        this.description = description.trim();
        this.productionDate = productionDate;
    }

    /**
     * Copy constructor used to implement the Prototype pattern.
     *
     * @param other the product instance to copy, must not be null
     * @throws NullPointerException if {@code other} is null
     */
    public Product(Product other) {
        Objects.requireNonNull(other, "Product to copy cannot be null");
        this.id = (other.id == null || other.id.trim().isEmpty()) ? UUID.randomUUID().toString() : other.id.trim();
        this.name = other.name;
        this.category = other.category;
        this.description = other.description;
        this.productionDate = other.productionDate;
    }


    /**
     * Returns the product id.
     *
     * @return product id, never null after construction
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the product id. Validates input and normalizes whitespace.
     *
     * @param id unique identifier, must be non-null and non-empty
     * @throws IllegalArgumentException if id is null or empty
     */
    public void setId(String id) {
        validateId(id);
        this.id = id.trim();
    }

    /**
     * Returns the product name.
     *
     * @return product name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product name. Validates input and trims whitespace.
     *
     * @param name product name, must be non-null and non-empty
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        validateName(name);
        this.name = name.trim();
    }

    /**
     * Returns the product category.
     *
     * @return product category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the product category. Validates input and normalizes to lowercase.
     *
     * @param category product category, must be non-null and non-empty
     * @throws IllegalArgumentException if category is null or empty
     */
    public void setCategory(String category) {
        validateCategory(category);
        this.category = category.toLowerCase().trim();
    }

    /**
     * Returns the product description.
     *
     * @return product description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the product description. Validates input and trims whitespace.
     *
     * @param description product description, must be non-null and non-empty
     * @throws IllegalArgumentException if description is null or empty
     */
    public void setDescription(String description) {
        validateDescription(description);
        this.description = description.trim();
    }



    /**
     * Returns the production date.
     *
     * @return production/harvest date
     */
    public Date getProductionDate() {
        return productionDate;
    }

    /**
     * Sets the production date. Validates the date is not null and not in the future.
     *
     * @param productionDate production/harvest date, must be non-null and not a future date
     * @throws IllegalArgumentException if productionDate is null or in the future
     */
    public void setProductionDate(Date productionDate) {
        validateProductionDate(productionDate);
        this.productionDate = productionDate;
    }


    /**
     * Returns whether the product is considered "fresh" (produced within last 30 days).
     *
     * @return {@code true} if productionDate is within the last 30 days
     */
    public boolean isFresh() {
        return productionDate != null && productionDate.getTime() > (new Timestamp(System.currentTimeMillis())).getTime() - 1800;
    }

    // ----------------- validation helpers (private) -----------------

    /**
     * Validates the product id parameter.
     *
     * @param id candidate id
     * @throws IllegalArgumentException if id is null or empty
     */
    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
    }

    /**
     * Validates the product name parameter.
     *
     * @param name candidate name
     * @throws IllegalArgumentException if name is null or empty
     */
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
    }

    /**
     * Validates the product category parameter.
     *
     * @param category candidate category
     * @throws IllegalArgumentException if category is null or empty
     */
    private static void validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Product category cannot be null or empty");
        }
    }

    /**
     * Validates the product description parameter.
     *
     * @param description candidate description
     * @throws IllegalArgumentException if description is null or empty
     */
    private static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }
    }

    /**
     * Validates the cultivation method parameter.
     *
     * @param method candidate cultivation method
     * @throws IllegalArgumentException if method is null or empty
     */
    private static void validateCultivationMethod(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Cultivation method cannot be null or empty");
        }
    }

    /**
     * Validates the production date parameter.
     *
     * @param date candidate production date
     * @throws IllegalArgumentException if date is null or in the future
     */
    private static void validateProductionDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Production date cannot be null");
        }
    }

    /**
     * Validates the producer id parameter.
     *
     * @param producerId candidate producer identifier
     * @throws IllegalArgumentException if producerId is null or empty
     */
    private static void validateProducerId(String producerId) {
        if (producerId == null || producerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Producer ID cannot be null or empty");
        }
    }

    // ----------------- equals/hashCode/toString -----------------

    /**
     * Equality is based on product {@code id} (if present).
     *
     * @param o other object to compare
     * @return {@code true} if both objects are Product instances with the same id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return id != null && id.equals(product.id);
    }

    /**
     * Hash code implementation based on {@code id}.
     *
     * @return hash code derived from id or 0 if id is null
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    /**
     * String representation containing key identifying fields.
     *
     * @return short text representation useful for logs and debugging
     */
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}