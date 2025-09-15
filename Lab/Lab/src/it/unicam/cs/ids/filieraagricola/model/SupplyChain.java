package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a supply chain (filiera) within the platform.
 *
 * <p>This class is a pure domain model following the Single Responsibility
 * Principle. It focuses solely on representing supply chain data without
 * business logic. All operations like finding organic products, checking
 * if active, and filtering by category are delegated to SupplyChainService.</p>
 */
public class SupplyChain {

    private String id;
    private String name;
    private String description;
    private List<Product> products;
    private LocalDateTime creationDate;
    private String territorialArea;

    /**
     * Default constructor for frameworks.
     * Initializes products to an empty list and sets current timestamp.
     */
    public SupplyChain() {
        this.id = UUID.randomUUID().toString();
        this.products = new ArrayList<>();
        this.creationDate = LocalDateTime.now();
    }

    /**
     * Main constructor with name and products.
     *
     * @param name     supply chain name
     * @param products associated products
     */
    public SupplyChain(String name, List<Product> products) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = "Supply chain for " + name;
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
        this.creationDate = LocalDateTime.now();
        this.territorialArea = "default";
    }

    /**
     * Full constructor with all fields.
     *
     * @param id              unique identifier
     * @param name            supply chain name
     * @param description     supply chain description
     * @param products        list of associated products
     * @param creationDate    creation timestamp
     * @param territorialArea territorial area
     */
    public SupplyChain(String id,
                       String name,
                       String description,
                       List<Product> products,
                       LocalDateTime creationDate,
                       String territorialArea) {
        this.id = (id == null || id.trim().isEmpty())
                ? UUID.randomUUID().toString()
                : id.trim();
        this.name = name;
        this.description = description;
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
        this.creationDate = creationDate != null ? creationDate : LocalDateTime.now();
        this.territorialArea = territorialArea;
    }

    /**
     * Copy constructor for creating a new instance from an existing chain.
     *
     * @param other the SupplyChain instance to copy
     * @throws NullPointerException if other is null
     */
    public SupplyChain(SupplyChain other) {
        Objects.requireNonNull(other, "SupplyChain to copy cannot be null");
        this.id = other.id;
        this.name = other.name;
        this.description = other.description;
        this.creationDate = other.creationDate;
        this.territorialArea = other.territorialArea;
        // Create a shallow copy of the products list
        this.products = new ArrayList<>(other.products);
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
     * Sets the unique identifier of the supply chain.
     *
     * @param id id to set
     */
    public void setId(String id) {
        this.id = id;
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
     * Sets the name of the supply chain.
     *
     * @param name supply chain name
     */
    public void setName(String name) {
        this.name = name;
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
     * Sets the description of the supply chain.
     *
     * @param description description text
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the products associated with this supply chain.
     *
     * <p>Note: Returns the internal list reference. For defensive copying
     * and filtering operations, use SupplyChainService methods.</p>
     *
     * @return list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Sets the list of products for this supply chain.
     *
     * @param products list of products
     */
    public void setProducts(List<Product> products) {
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
    }

    /**
     * Returns the creation timestamp of the supply chain.
     *
     * @return creation date/time
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param creationDate creation timestamp
     */
    public void setCreationDate(LocalDateTime creationDate) {
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
     * Sets the territorial area.
     *
     * @param territorialArea territorial area
     */
    public void setTerritorialArea(String territorialArea) {
        this.territorialArea = territorialArea;
    }

    /**
     * Equality is based on ID when present.
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
     * Hash code based on ID.
     *
     * @return hash code integer
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
                ", territorialArea='" + territorialArea + '\'' +
                '}';
    }
}