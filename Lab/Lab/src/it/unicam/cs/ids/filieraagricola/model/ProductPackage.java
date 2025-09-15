package it.unicam.cs.ids.filieraagricola.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a package (bundle) of products in the agricultural supply chain.
 *
 * <p>This class is a pure domain model following the Single Responsibility
 * Principle. It focuses solely on representing a collection of products
 * without business logic. All operations like package validation, product
 * management, and business rules are delegated to PackageService.</p>
 */
public class ProductPackage {

    private String id;
    private String name;
    private List<Product> products;

    /**
     * Default constructor for frameworks.
     * Creates an empty product list and generates a unique ID.
     */
    public ProductPackage() {
        this.id = UUID.randomUUID().toString();
        this.products = new ArrayList<>();
    }

    /**
     * Main constructor that creates a package with name and products.
     *
     * @param name     package name
     * @param products list of products in the package
     */
    public ProductPackage(String name, List<Product> products) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
    }

    /**
     * Full constructor with all fields including ID.
     *
     * @param id       package identifier
     * @param name     package name
     * @param products list of products in the package
     */
    public ProductPackage(String id, String name, List<Product> products) {
        this.id = (id == null || id.trim().isEmpty())
                ? UUID.randomUUID().toString()
                : id.trim();
        this.name = name;
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
    }

    /**
     * Copy constructor for creating a new instance from an existing package.
     *
     * @param other the ProductPackage instance to copy
     * @throws NullPointerException if other is null
     */
    public ProductPackage(ProductPackage other) {
        Objects.requireNonNull(other, "ProductPackage to copy cannot be null");
        this.id = other.id;
        this.name = other.name;
        // Create a shallow copy of the products list
        this.products = new ArrayList<>(other.products);
    }

    /**
     * Returns the package ID.
     *
     * @return package identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the package ID.
     *
     * @param id package identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the package name.
     *
     * @return package name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the package name.
     *
     * @param name package name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of products in this package.
     *
     * <p>Note: Returns the internal list reference. For defensive copying,
     * use PackageService methods.</p>
     *
     * @return list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Sets the products list for this package.
     *
     * @param products list of products
     */
    public void setProducts(List<Product> products) {
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
    }

    /**
     * Equality is based on the package ID.
     *
     * <p>Two packages with the same ID are considered equal for
     * domain-level identity.</p>
     *
     * @param o other object to compare
     * @return true if equal by ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductPackage that = (ProductPackage) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Hash code computed from package ID.
     *
     * @return hash code integer
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    /**
     * Short textual representation suitable for logs and debugging.
     *
     * @return brief string describing the package
     */
    @Override
    public String toString() {
        return "ProductPackage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", productsCount=" + (products == null ? 0 : products.size()) +
                '}';
    }
}