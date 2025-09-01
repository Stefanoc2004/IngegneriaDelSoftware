package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.SupplyChain;

import java.util.*;

/**
 * Service responsible for managing supply chains and products.
 *
 * <p>The service stores defensive copies and returns clones to callers, avoiding
 * exposure of internal mutable state. Product cloning is used to enforce the
 * Prototype pattern contract. Public list-returning methods produce lists via
 * {@link java.util.stream.Stream#toList()} to avoid leaking internal collections.</p>
 */
public class SupplyChainService {

    private final List<SupplyChain> supplyChainList;
    private final List<Product> productList;

    /**
     * Constructs an empty SupplyChainService.
     */
    public SupplyChainService() {
        this.supplyChainList = new ArrayList<>();
        this.productList = new ArrayList<>();
    }

    /**
     * Acquires a product into the system.
     *
     * <p>Stores a defensive clone internally and returns a clone to the caller.</p>
     *
     * @param product product to acquire (must not be null)
     * @return a clone of the product stored
     * @throws IllegalArgumentException if {@code product} is null
     */
    public Product acquireProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");

        if (!productList.contains(product)) {
            productList.add(product.clone());
        }

        return product.clone();
    }

    /**
     * Sells (removes) a product from the managed collection.
     *
     * @param product product to sell (must match by equality/id)
     * @return {@code true} if the product was removed; {@code false} otherwise
     * @throws IllegalArgumentException if {@code product} is null or not available
     */
    public boolean sellProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        if (!productList.contains(product)) throw new IllegalArgumentException("Product is not available in the supply chain");
        return productList.remove(product);
    }

    /**
     * Returns a list of managed products as clones.
     *
     * <p>Each element in the returned list is a defensive clone produced by
     * {@link Product#clone()}; the returned list itself is the result of {@code Stream.toList()}.</p>
     *
     * @return list of product clones
     */
    public List<Product> getProductList() {
        return productList.stream()
                .map(Product::clone)
                .toList();
    }

    /**
     * Returns a list of managed supply chains as clones.
     *
     * <p>Each element in the returned list is a defensive clone produced by
     * {@link SupplyChain#clone()}; the returned list itself is the result of {@code Stream.toList()}.</p>
     *
     * @return list of supply chain clones
     */
    public List<SupplyChain> getSupplyChainList() {
        return supplyChainList.stream()
                .map(SupplyChain::clone)
                .toList();
    }

    /**
     * Creates a new supply chain with the given name and products.
     *
     * <p>The created supply chain is stored internally and a clone is returned.</p>
     *
     * @param supplyChainName supply chain name (must not be null/empty)
     * @param products        products to include (must not be null)
     * @return clone of the created SupplyChain
     * @throws IllegalArgumentException if inputs are invalid
     */
    public SupplyChain createSupplyChain(String supplyChainName, List<Product> products) {
        if (supplyChainName == null || supplyChainName.trim().isEmpty()) throw new IllegalArgumentException("Supply chain name cannot be null or empty");
        if (products == null) throw new IllegalArgumentException("Product list cannot be null");

        var supplyChain = buildSupplyChain(supplyChainName, products);
        supplyChainList.add(supplyChain);
        return supplyChain.clone();
    }

    /**
     * Finds supply chains whose name contains the given pattern (case-insensitive).
     *
     * <p>Returned list contains clones of matching supply chains.</p>
     *
     * @param name search pattern (may not be null/empty)
     * @return list of matching supply chain clones (empty list if none)
     */
    public List<SupplyChain> findSupplyChainsByName(String name) {
        if (name == null || name.trim().isEmpty()) return Collections.emptyList();
        String norm = name.toLowerCase().trim();
        return supplyChainList.stream()
                .filter(sc -> sc.getName().toLowerCase().contains(norm))
                .map(SupplyChain::clone)
                .toList();
    }

    /**
     * Finds products by category and returns clones of matching products.
     *
     * <p>Returned list contains defensive clones of matched products.</p>
     *
     * @param category category to filter by (must not be null/empty)
     * @return list of product clones matching the category (empty list if none)
     */
    public List<Product> findProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) return Collections.emptyList();
        String norm = category.toLowerCase().trim();
        return productList.stream()
                .filter(product -> product.getCategory() != null && product.getCategory().toLowerCase().contains(norm))
                .map(Product::clone)
                .toList();
    }

    /* ----------------- private helpers ----------------- */

    /**
     * Builds a new SupplyChain instance. Products are cloned to ensure defensive isolation.
     *
     * @param name     supply chain name
     * @param products source products (must not be null)
     * @return a new SupplyChain instance (internal instance)
     */
    private SupplyChain buildSupplyChain(String name, List<Product> products) {
        List<Product> copies = products.stream()
                .map(Product::clone)
                .toList();
        return new SupplyChain(name, copies);
    }
}
