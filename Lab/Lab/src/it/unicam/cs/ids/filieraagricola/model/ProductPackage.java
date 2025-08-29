package it.unicam.cs.ids.filieraagricola.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a package (bundle) of products in the agricultural supply chain.
 *
 * <p>This class implements the Prototype pattern via {@link #copy()} to allow
 * cloning. Collections are defensively copied on set/construct and {@link #getProducts()}
 * returns an unmodifiable view to preserve encapsulation.</p>
 */
public class ProductPackage implements Prototype<ProductPackage> {

    private String name;
    private List<Product> products;

    /**
     * Default constructor for frameworks and for manual construction.
     * Creates an empty product list.
     */
    public ProductPackage() {
        this.products = new ArrayList<>();
    }

    /**
     * Main constructor that validates inputs and stores defensive copies
     * of collection arguments.
     *
     * @param name     package name, must be non-null and non-empty
     * @param products list of products contained in the package, must be non-null
     * @throws IllegalArgumentException if name is null/empty or products is null
     */
    public ProductPackage(String name, List<Product> products) {
        setName(name);
        setProducts(products);
    }

    /**
     * Copy constructor used to implement the Prototype pattern.
     *
     * @param other the ProductPackage instance to copy, must not be null
     * @throws NullPointerException if {@code other} is null
     */
    public ProductPackage(ProductPackage other) {
        Objects.requireNonNull(other, "ProductPackage to copy cannot be null");
        this.name = other.name;
        // create defensive copy of products
        this.products = new ArrayList<>();
        if (other.products != null) {
            other.products.forEach(p -> this.products.add(p == null ? null : p.copy()));
        }
    }

    /**
     * Creates and returns a copy of this ProductPackage.
     *
     * @return a new ProductPackage that duplicates this instance
     */
    @Override
    public ProductPackage copy() {
        return new ProductPackage(this);
    }

    /**
     * Returns the package name.
     *
     * @return package name string
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the package name. Validates and normalizes input.
     *
     * @param name package name, must be non-null and non-empty
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Package name cannot be null or empty");
        }
        this.name = name.trim();
    }

    /**
     * Returns an unmodifiable view of products contained in this package.
     * Each Product instance in the internal list is the internal instance;
     * to avoid leaking mutable objects the caller should treat elements as read-only
     * or the service layer should return copies (we already copy in services).
     *
     * @return unmodifiable list of products
     */
    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    /**
     * Sets the products list for this package. Performs defensive copying and
     * uses {@link Product#copy()} for each element to avoid sharing references.
     *
     * @param products list of products (must not be null)
     * @throws IllegalArgumentException if products is null
     */
    public void setProducts(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("Products list cannot be null");
        }
        // defensive deep copy using Prototype.copy()
        List<Product> copy = new ArrayList<>(products.size());
        for (Product p : products) {
            copy.add(p == null ? null : p.copy());
        }
        this.products = copy;
    }

    /**
     * Adds a product to the package. The product is defensively copied before storage.
     *
     * @param product product to add (may be null)
     */
    public void addProduct(Product product) {
        this.products.add(product == null ? null : product.copy());
    }

    /**
     * Removes a product from the package using equality (based on Product.equals).
     *
     * @param product product to remove
     * @return true if removed, false otherwise
     */
    public boolean removeProduct(Product product) {
        return this.products.remove(product);
    }

    /**
     * Returns the number of products contained in the package.
     *
     * @return product count (0 if none)
     */
    public int getProductCount() {
        return this.products == null ? 0 : this.products.size();
    }

    /**
     * Equality is based on the package {@code name}. Two packages with the same
     * name are considered equal for domain-level identity. This matches the
     * lightweight identity used in earlier designs (no database id present).
     *
     * @param o other object to compare
     * @return true if equal by name
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductPackage that = (ProductPackage) o;
        return Objects.equals(name, that.name);
    }

    /**
     * Hash code computed from package name.
     *
     * @return hash code integer
     */
    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }

    /**
     * Short textual representation suitable for logs and debugging.
     *
     * @return brief string describing the package
     */
    @Override
    public String toString() {
        return "ProductPackage{" +
                "name='" + name + '\'' +
                ", productsCount=" + (products == null ? 0 : products.size()) +
                '}';
    }
}