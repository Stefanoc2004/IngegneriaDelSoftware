package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an agricultural product within the supply chain platform.
 *
 * <p>This class is a pure domain model following the Single Responsibility
 * Principle. It focuses solely on representing product data without business
 * logic. All business operations (checking if organic, fresh, etc.) are
 * delegated to the ProductService.</p>
 */
public class Product {

    private String id;
    private String name;
    private String category;
    private String description;
    private String cultivationMethod;
    private String certifications;
    private LocalDate productionDate;
    private String producerId;

    /**
     * Default no-argument constructor for frameworks (e.g., Spring, JPA).
     * Generates a unique ID for the product.
     */
    public Product() {
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Full constructor that creates a product with all fields.
     * If the ID is null or empty, a new UUID is generated.
     *
     * @param id                unique identifier for the product
     * @param name              commercial product name
     * @param category          product category (e.g., "vegetables")
     * @param description       human-readable description
     * @param cultivationMethod cultivation method (e.g., "organic")
     * @param certifications    certifications information
     * @param productionDate    production/harvest date
     * @param producerId        identifier of the producer
     */
    public Product(String id,
                   String name,
                   String category,
                   String description,
                   String cultivationMethod,
                   String certifications,
                   LocalDate productionDate,
                   String producerId) {
        this.id = (id == null || id.trim().isEmpty())
                ? UUID.randomUUID().toString()
                : id.trim();
        this.name = name;
        this.category = category;
        this.description = description;
        this.cultivationMethod = cultivationMethod;
        this.certifications = certifications;
        this.productionDate = productionDate;
        this.producerId = producerId;
    }

    /**
     * Copy constructor for creating a new instance from an existing product.
     *
     * @param other the product instance to copy
     * @throws NullPointerException if other is null
     */
    public Product(Product other) {
        Objects.requireNonNull(other, "Product to copy cannot be null");
        this.id = other.id;
        this.name = other.name;
        this.category = other.category;
        this.description = other.description;
        this.cultivationMethod = other.cultivationMethod;
        this.certifications = other.certifications;
        this.productionDate = other.productionDate;
        this.producerId = other.producerId;
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
     * Sets the product id.
     *
     * @param id unique identifier
     */
    public void setId(String id) {
        this.id = id;
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
     * Sets the product name.
     *
     * @param name product name
     */
    public void setName(String name) {
        this.name = name;
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
     * Sets the product category.
     *
     * @param category product category
     */
    public void setCategory(String category) {
        this.category = category;
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
     * Sets the product description.
     *
     * @param description product description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the cultivation method.
     *
     * @return cultivation method string
     */
    public String getCultivationMethod() {
        return cultivationMethod;
    }

    /**
     * Sets the cultivation method.
     *
     * @param cultivationMethod cultivation method
     */
    public void setCultivationMethod(String cultivationMethod) {
        this.cultivationMethod = cultivationMethod;
    }

    /**
     * Returns certifications string.
     *
     * @return certifications string (may be null or empty)
     */
    public String getCertifications() {
        return certifications;
    }

    /**
     * Sets the certifications string.
     *
     * @param certifications certifications information
     */
    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    /**
     * Returns the production date.
     *
     * @return production/harvest date
     */
    public LocalDate getProductionDate() {
        return productionDate;
    }

    /**
     * Sets the production date.
     *
     * @param productionDate production/harvest date
     */
    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    /**
     * Returns the producer identifier.
     *
     * @return producer id
     */
    public String getProducerId() {
        return producerId;
    }

    /**
     * Sets the producer identifier.
     *
     * @param producerId producer id
     */
    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    /**
     * Equality is based on product id.
     *
     * @param o other object to compare
     * @return true if both objects are Product instances with the same id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return id != null && id.equals(product.id);
    }

    /**
     * Hash code implementation based on id.
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
                ", producerId='" + producerId + '\'' +
                '}';
    }
}