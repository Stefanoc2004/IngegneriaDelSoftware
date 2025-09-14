package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.List;

/**
 * Represents a package (bundle) of {@link Product products} in the agricultural supply-chain platform.
 *
 * <p>This class is a JPA entity used to group multiple {@link Product} instances as a single
 * offer or package. It intentionally preserves the original shape and behaviour:
 * no defensive-copying, no additional helper methods and no behavioural changes were introduced —
 * only JavaDoc comments have been added/updated.</p>
 *
 * <p>Fields:
 * <ul>
 *   <li>{@code id} - primary identifier (annotated with {@code @Id})</li>
 *   <li>{@code name} - human-readable package name</li>
 *   <li>{@code products} - list of products included in the package (mapped with {@code @ManyToMany})</li>
 * </ul>
 * </p>
 */
@Entity
public class ProductPackage {

    @Id
    private String id;
    private String name;
    @ManyToMany
    private List<Product> products;

    /**
     * Default no-argument constructor required by JPA and other frameworks.
     * Creates an empty instance where fields are left uninitialized (may be {@code null})
     * until populated via setters or by the persistence provider.
     */
    public ProductPackage() {
    }

    /**
     * Parameterized constructor that initializes all fields.
     *
     * <p>Note: this constructor does not perform validation or defensive copying;
     * callers who require defensive behaviour should copy inputs externally before
     * calling this constructor.</p>
     *
     * @param id       unique identifier for the package (may be {@code null} prior to persistence)
     * @param name     human-readable package name (may be {@code null})
     * @param products list of products included in this package (may be {@code null})
     */
    public ProductPackage(String id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    /**
     * Returns the package identifier.
     *
     * @return the package id, may be {@code null} before persistence or if not set
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the package identifier.
     *
     * @param id unique identifier to set (may be {@code null} if the id is assigned by persistence)
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the human-readable name of the package.
     *
     * @return package name, may be {@code null} if not set
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the human-readable name for the package.
     *
     * @param name package name to set (may be {@code null})
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of products contained in the package.
     *
     * <p><b>Important:</b> this method returns the internal list reference as-is
     * (no defensive copy). Consumers must not assume immutability and should avoid
     * modifying the returned list unless intended.</p>
     *
     * @return list of {@link Product} instances contained in this package, may be {@code null}
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Sets the list of products contained in the package.
     *
     * <p>This method assigns the provided list reference directly to the internal field;
     * no defensive copy is performed.</p>
     *
     * @param products list of products to set (may be {@code null})
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Returns a short string representation containing key identifying fields.
     *
     * @return compact textual representation for logging and debugging
     */
    @Override
    public String toString() {
        return "ProductPackage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", products=" + products +
                '}';
    }
}
