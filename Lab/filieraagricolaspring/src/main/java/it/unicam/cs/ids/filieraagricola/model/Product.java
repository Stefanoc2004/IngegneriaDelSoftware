package it.unicam.cs.ids.filieraagricola.model;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Represents an agricultural product within the supply-chain platform.
 *
 * <p>This class is a JPA entity used by the Spring Boot application. It provides:
 * <ul>
 *   <li>a no-argument constructor for frameworks (JPA)</li>
 *   <li>a full parameter constructor with validation</li>
 *   <li>defensive validation utilities</li>
 *   <li>getters/setters with input validation</li>
 * </ul>
 *
 * <p>All public API methods and the main private helpers are documented in English
 * and intentionally reflect the current implementation behaviour so the documentation
 * stays consistent with runtime checks.</p>
 */
@Entity
public class Product  {

    @Id
    private String id;
    private String name;
    private String category;
    private String description;
    private Date productionDate;
    private double price;

    /**
     * Default no-argument constructor for frameworks (e.g., Spring Data / JPA).
     * Generates a random UUID for the identifier to ensure non-null IDs when
     * entities are created programmatically. Other fields remain uninitialized
     * (null) and should be set via setters or the full constructor.
     */
    public Product() {
        this.id = UUID.randomUUID().toString(); // Automatically generate ID
    }

    /**
     * Full constructor that validates and normalizes input values.
     * <p>
     * Behaviour notes:
     * <ul>
     *   <li>If {@code id} is null or blank a new UUID string is generated.</li>
     *   <li>String fields are trimmed (and {@code category} is normalized to lower-case).</li>
     *   <li>Validation performed by this constructor is aligned with the private
     *       helper methods used here (see {@link #validateProductionDate(Date)}).</li>
     * </ul>
     *
     * @param id             unique identifier for the product; if {@code null} or blank a new UUID is generated
     * @param name           commercial product name; must be non-null and non-empty
     * @param category       product category (for example "vegetables"); must be non-null and non-empty
     * @param description    human-readable description; must be non-null and non-empty
     * @param productionDate production/harvest date; must be non-null (current implementation does not
     *                       enforce a "not in the future" check — see {@link #validateProductionDate(Date)}
     * @throws IllegalArgumentException if any required argument is invalid according to the current validations
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
     * Returns the product identifier.
     *
     * @return product id; may be {@code null} only prior to initialization, but is set in all constructors
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the product identifier.
     *
     * <p>The provided id is validated (non-null, non-empty) and trimmed. Use with
     * care: changing the identifier of a persisted entity may break identity semantics.</p>
     *
     * @param id unique identifier to set; must not be {@code null} or empty
     * @throws IllegalArgumentException if {@code id} is {@code null} or empty
     */
    public void setId(String id) {
        validateId(id);
        this.id = id.trim();
    }

    /**
     * Returns the product name.
     *
     * @return product name, may be {@code null} if not set
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product name.
     *
     * @param name non-null, non-empty product name
     * @throws IllegalArgumentException if {@code name} is {@code null} or empty
     */
    public void setName(String name) {
        validateName(name);
        this.name = name.trim();
    }

    /**
     * Returns the product category.
     *
     * @return category string (normalized to lower-case by constructors/setters)
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the product category.
     *
     * <p>The category is normalized to lower-case and trimmed before assignment.</p>
     *
     * @param category non-null, non-empty category
     * @throws IllegalArgumentException if {@code category} is {@code null} or empty
     */
    public void setCategory(String category) {
        validateCategory(category);
        this.category = category.toLowerCase().trim();
    }

    /**
     * Returns the product description.
     *
     * @return description string, may be {@code null} if not set
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the product description.
     *
     * @param description non-null, non-empty description text
     * @throws IllegalArgumentException if {@code description} is {@code null} or empty
     */
    public void setDescription(String description) {
        validateDescription(description);
        this.description = description.trim();
    }



    /**
     * Returns the production (harvest) date of the product.
     *
     * @return production date as {@link java.sql.Date}, may be {@code null} if not set
     */
    public Date getProductionDate() {
        return productionDate;
    }

    /**
     * Sets the production (harvest) date.
     *
     * <p>Current implementation validates that the provided {@code Date} is not null.
     * The constructor documentation previously stated "not in the future"; however the
     * current internal validator {@link #validateProductionDate(Date)} only enforces non-null.
     * If you need to enforce "not in the future" behaviour, update {@link #validateProductionDate(Date)} accordingly.</p>
     *
     * @param productionDate production date; must not be {@code null} according to current validation
     * @throws IllegalArgumentException if {@code productionDate} is {@code null}
     */
    public void setProductionDate(Date productionDate) {
        validateProductionDate(productionDate);
        this.productionDate = productionDate;
    }


    // ----------------- validation helpers (private) -----------------

    /**
     * Validate the product id parameter.
     *
     * @param id candidate id
     * @throws IllegalArgumentException if {@code id} is {@code null} or empty
     */
    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
    }

    /**
     * Validate the product name parameter.
     *
     * @param name candidate name
     * @throws IllegalArgumentException if {@code name} is {@code null} or empty
     */
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
    }

    /**
     * Validate the product category parameter.
     *
     * @param category candidate category
     * @throws IllegalArgumentException if {@code category} is {@code null} or empty
     */
    private static void validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Product category cannot be null or empty");
        }
    }

    /**
     * Validate the product description parameter.
     *
     * @param description candidate description
     * @throws IllegalArgumentException if {@code description} is {@code null} or empty
     */
    private static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }
    }


    /**
     * Validate the production date parameter.
     *
     * <p>Current behaviour: only checks that the {@code Date} instance is not {@code null}.
     * If you need to disallow future dates, extend this validator with an explicit check like:
     * {@code if (date.after(new Date(System.currentTimeMillis())) ) throw ...}.</p>
     *
     * @param date candidate production date
     * @throws IllegalArgumentException if {@code date} is {@code null}
     */
    private static void validateProductionDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Production date cannot be null");
        }
    }



    // ----------------- equals/hashCode/toString -----------------

    /**
     * Equality is based on product {@code id} when present.
     *
     * @param o other object to compare
     * @return {@code true} if both objects are {@code Product} instances with the same non-null id
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
     * Short textual representation containing key identifying fields.
     *
     * @return compact string useful for logs and debugging
     */
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    /**
     * Returns the unit price associated with this product.
     */
    public double getPrice() {
        return price;
    }
    /**
     * Sets the unit price associated with this product without additional validation.
     *
     * @param price monetary value to assign
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
