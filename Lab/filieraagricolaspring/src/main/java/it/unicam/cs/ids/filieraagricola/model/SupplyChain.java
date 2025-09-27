package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a supply chain (filiera) within the platform.
 *
 * <p>Encapsulates descriptive metadata, associated {@link Product} instances and
 * geolocated {@link SupplyChainPoint points}. The class exposes validation
 * helpers and simple queries (such as category aggregation) while keeping state
 * management minimal and consistent with the current implementation.</p>
 */
@Entity
public class SupplyChain  {

    @Id
    private String id;
    private String name;
    private String description;
    @OneToMany
    private List<Product> products;
    private Timestamp creationDate;
    private String territorialArea;
    @OneToMany
    private List<SupplyChainPoint> points;




    /**
     * Default constructor for frameworks. Initializes products to an empty list
     * and creationDate to {@link LocalDateTime#now()}.
     */
    public SupplyChain() {
        this.products = new ArrayList<>();
        this.points = new ArrayList<>();
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.territorialArea = "default";
        this.name = "unnamed";
        this.description = "";
        this.id = generateId(this.name, this.territorialArea);
    }

    /**
     * Full constructor validating all fields and creating defensive copies.
     *
     * @param id              unique identifier, must be non-null and non-empty
     * @param name            supply chain name, must be non-null and non-empty
     * @param description     supply chain description, must be non-null and non-empty
     * @param products        list of associated products, must be non-null
     * @param creationDate    creation timestamp, must be non-null
     * @param territorialArea territorial area, must be non-null and non-empty
     * @throws IllegalArgumentException if any required parameter is invalid
     */
    public SupplyChain(String id,
                       String name,
                       String description,
                       List<Product> products,
                       Timestamp creationDate,
                       String territorialArea) {
        validateId(id);
        validateName(name);
        validateDescription(description);
        validateProducts(products);
        validateCreationDate(creationDate);
        validateTerritorialArea(territorialArea);

        this.id = id.trim();
        this.name = name.trim();
        this.description = description.trim();
        // defensive deep-copy of products using Product.copy()
        this.products = products;
        this.creationDate = creationDate;
        this.territorialArea = territorialArea.trim();
    }

    /**
     * Convenience constructor used by services: generates id and sets defaults.
     *
     * @param name     supply chain name, must be non-null and non-empty
     * @param products associated products (must not be null)
     */
    public SupplyChain(String name, List<Product> products) {
        this(generateId(name, "default"),
                name,
                "Supply chain for " + name,
                products == null ? Collections.emptyList() : products,
                new Timestamp(System.currentTimeMillis()),
                "default");
    }





    /**
     * Returns the unique identifier of the supply chain.
     *
     * @return supply chain id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the supply chain. Validates input.
     *
     * @param id id to set (must be non-null and non-empty)
     * @throws IllegalArgumentException if id is null or empty
     */
    public void setId(String id) {
        validateId(id);
        this.id = id.trim();
    }

    /**
     * Returns the name of the supply chain.
     *
     * @return supply chain name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the supply chain. Validates and normalizes input.
     *
     * @param name supply chain name (must be non-null and non-empty)
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        validateName(name);
        this.name = name.trim();
    }

    /**
     * Returns the description of the supply chain.
     *
     * @return description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the supply chain. Validates input.
     *
     * @param description description text (must be non-null and non-empty)
     * @throws IllegalArgumentException if description is null or empty
     */
    public void setDescription(String description) {
        validateDescription(description);
        this.description = description.trim();
    }

    /**
     * Returns the list of products associated with this supply chain.
     *
     * <p>Note: this implementation returns the internal list reference as-is.</p>
     */
    public List<Product> getProducts() {
        return products;
    }

    /*
    /**
     * Sets the list of products for this supply chain. Performs defensive copying
     * by calling {@link Product#clone()} on each element.
     *
     * @param products list of products (must not be null)
     * @throws IllegalArgumentException if products is null

    public void setProducts(List<Product> products) {
        validateProducts(products);
        List<Product> copy = new ArrayList<>(products.size());
        for (Product p : products) {
            copy.add(p == null ? null : p.clone());
        }
        this.products = copy;
    }
    */

    /**
     * Sets the products associated with this supply chain.
     *
     * <p>No defensive copy is performed in the current implementation.</p>
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Returns the creation timestamp of the supply chain.
     *
     * @return creation date/time
     */
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation timestamp. Validates input.
     *
     * @param creationDate creation timestamp (must not be null)
     * @throws IllegalArgumentException if creationDate is null
     */
    public void setCreationDate(Timestamp creationDate) {
        validateCreationDate(creationDate);
        this.creationDate = creationDate;
    }

    /**
     * Returns the territorial area associated with this supply chain.
     *
     * @return territorial area string
     */
    public String getTerritorialArea() {
        return territorialArea;
    }

    /**
     * Sets the territorial area. Validates and normalizes input.
     *
     * @param territorialArea territorial area (must be non-null and non-empty)
     * @throws IllegalArgumentException if territorialArea is null or empty
     */
    public void setTerritorialArea(String territorialArea) {
        validateTerritorialArea(territorialArea);
        this.territorialArea = territorialArea.trim();
    }

    /**
     * Returns the number of products associated with this supply chain.
     *
     * @return product count
     */
    public int getProductCount() {
        return products == null ? 0 : products.size();
    }

    /**
     * Checks whether the supply chain contains the specified product.
     * Equality relies on {@link Product#equals(Object)} (typically id-based).
     *
     * @param product product to check (may be null)
     * @return true if contained, false otherwise
     */
    public boolean containsProduct(Product product) {
        return products != null && products.contains(product);
    }

    /*
    /**
     * Returns a list of organic products. Each returned element is a defensive copy.
     *
     * @return unmodifiable list of copies of organic products
    /*
    public List<Product> getOrganicProducts() {
        List<Product> result = products.stream()
                .filter(Objects::nonNull)
                .filter(Product::isOrganic)
                .map(Product::clone)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }
    */

    /*
    /**
     * Returns a list of certified products. Each returned element is a defensive copy.
     *
     * @return unmodifiable list of copies of certified products
    /*
    public List<Product> getCertifiedProducts() {
        List<Product> result = products.stream()
                .filter(Objects::nonNull)
                .filter(Product::hasCertifications)
                .map(Product::clone)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

     */

    /*
    /**
     * Returns products that match the provided category. Returned elements are copies.
     *
     * @param category category to filter by (case-insensitive)
     * @return unmodifiable list of copies of matching products; empty list if category invalid
    /* public List<Product> getProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String normalized = category.trim().toLowerCase();
        List<Product> result = products.stream()
                .filter(Objects::nonNull)
                .map(Product::clone)
                .filter(p -> p.getCategory() != null && p.getCategory().toLowerCase().equals(normalized))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }
    */

    /*
    /**
     * Returns products considered "fresh" (produced within the last 30 days).
     * Each returned product is a defensive copy.
     *
     * @return unmodifiable list of copies of fresh products
    /* public List<Product> getFreshProducts() {
        List<Product> result = products.stream()
                .filter(Objects::nonNull)
                .filter(Product::isFresh)
                .map(Product::clone)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }
    */

    /**
     * Checks whether this supply chain is active. Active is defined as having at least
     * one product and a creation date not older than 6 months.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        long sixMonthsMs = 6L * 30L * 24L * 60L * 60L * 1000L; // ~6 mesi in ms (approssimazione)
        return products != null && !products.isEmpty()
                && creationDate != null
                && creationDate.getTime() > System.currentTimeMillis() - sixMonthsMs;
    }

    /**
     * Returns the set of unique product categories present in this supply chain.
     *
     * @return unmodifiable set of category strings
     */
    public Set<String> getUniqueCategories() {
        Set<String> set = products.stream()
                .map(Product::getCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return Collections.unmodifiableSet(set);
    }

    // ----------------- private validation helpers -----------------

    /**
     * Validates the supply chain id parameter.
     *
     * @param id candidate id
     * @throws IllegalArgumentException if id is null or empty
     */
    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Supply chain ID cannot be null or empty");
        }
    }

    /**
     * Validates the supply chain name parameter.
     *
     * @param name candidate name
     * @throws IllegalArgumentException if name is null or empty
     */
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Supply chain name cannot be null or empty");
        }
    }

    /**
     * Validates the supply chain description parameter.
     *
     * @param description candidate description
     * @throws IllegalArgumentException if description is null or empty
     */
    private static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Supply chain description cannot be null or empty");
        }
    }

    /**
     * Validates the products list parameter.
     *
     * @param products candidate products list
     * @throws IllegalArgumentException if products is null
     */
    private static void validateProducts(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("Products list cannot be null");
        }
    }

    /**
     * Validates the creation date parameter.
     *
     * @param creationDate candidate creation timestamp
     * @throws IllegalArgumentException if creationDate is null
     */
    private static void validateCreationDate(Timestamp creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException("Creation date cannot be null");
        }
    }

    /**
     * Validates the territorial area parameter.
     *
     * @param area candidate territorial area
     * @throws IllegalArgumentException if area is null or empty
     */
    private static void validateTerritorialArea(String area) {
        if (area == null || area.trim().isEmpty()) {
            throw new IllegalArgumentException("Territorial area cannot be null or empty");
        }
    }

    /**
     * Generates a unique id string using name and area plus a timestamp.
     *
     * @param name supply chain name
     * @param area territorial area
     * @return generated id string
     */
    private static String generateId(String name, String area) {
        return (name + "_" + area + "_" + System.currentTimeMillis())
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }

    // ----------------- equals/hashCode/toString -----------------

    /**
     * Equality is based on {@code id} when present.
     *
     * @param o other object
     * @return true if both SupplyChain have the same id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupplyChain that = (SupplyChain) o;
        return id != null && id.equals(that.id);
    }

    /**
     * Hash code based on {@code id}.
     *
     * @return hash code integer (0 if id null)
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    /**
     * Compact string representation with key identifying fields.
     *
     * @return brief textual representation
     */
    @Override
    public String toString() {
        return "SupplyChain{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", productsCount=" + (products == null ? 0 : products.size()) +
                '}';
    }


    /** Returns the geolocated points of this supply chain. */
    public List<SupplyChainPoint> getPoints() {
        return points;
    }

    /** Sets the geolocated points of this supply chain. */
    public void setPoints(List<SupplyChainPoint> points) {
        this.points = points;
    }
}