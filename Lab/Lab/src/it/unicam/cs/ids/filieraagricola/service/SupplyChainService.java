package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.SupplyChain;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for managing the agricultural supply chain (filiera).
 *
 * <p>This service uses defensive copying for Product objects (via {@code Product.clone()})
 * to preserve internal consistency and implement the Prototype pattern requirement.
 * Collections returned by this service are unmodifiable or contain copies to prevent
 * accidental external modification.</p>
 */
public class SupplyChainService {

    private final List<SupplyChain> supplyChainList;
    private final List<Product> productList;

    /**
     * Constructs a new SupplyChainService with empty collections.
     */
    public SupplyChainService() {
        this.supplyChainList = new ArrayList<>();
        this.productList = new ArrayList<>();
    }

    /**
     * Acquires a product and adds a defensive copy to the managed product collection.
     * If a product with the same id already exists, it is not duplicated.
     *
     * @param product the product to acquire, must not be null
     * @return the acquired product instance (copy) stored internally
     * @throws IllegalArgumentException if product is null
     */
    public Product acquireProduct(Product product) {
        validateProduct(product);

        // Use a defensive copy for internal storage
        if (!productList.contains(product)) {
            productList.add(product.clone());
        }

        // Return a copy to the caller as well (to avoid exposing internal instance)
        return product.clone();
    }

    /**
     * Sells a product by removing it from the managed collection.
     *
     * @param product the product to sell (matching by id), must not be null
     * @return true if the product was successfully sold (removed), false otherwise
     * @throws IllegalArgumentException if product is null or not available
     */
    public boolean sellProduct(Product product) {
        validateProduct(product);
        validateProductAvailability(product);

        return productList.remove(product);
    }

    /**
     * Retrieves all products currently in the supply chain as defensive copies.
     *
     * @return an unmodifiable list containing copies of managed products
     */
    public List<Product> getProductList() {
        return Collections.unmodifiableList(
                productList.stream()
                        .map(Product::clone)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Retrieves all supply chains managed by this service as defensive copies.
     *
     * @return an unmodifiable list containing copies of managed supply chains
     */
    public List<SupplyChain> getSupplyChainList() {
        return Collections.unmodifiableList(
                supplyChainList.stream()
                        .map(SupplyChain::clone)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Creates a new supply chain and adds it to the managed collection.
     * Products passed to the new supply chain are copied to avoid sharing references.
     *
     * @param supplyChainName the name of the supply chain, must not be null or empty
     * @param products        the products associated with this supply chain (must not be null)
     * @return the created SupplyChain instance (internal instance)
     * @throws IllegalArgumentException if supplyChainName is invalid or products is null
     */
    public SupplyChain createSupplyChain(String supplyChainName, List<Product> products) {
        validateSupplyChainName(supplyChainName);
        validateProductList(products);

        var supplyChain = buildSupplyChain(supplyChainName, products);
        supplyChainList.add(supplyChain);

        return supplyChain;
    }

    /**
     * Finds supply chains by name using partial matching.
     *
     * @param name the name pattern to search for
     * @return a list of matching supply chains; empty list if name is null/empty
     */
    public List<SupplyChain> findSupplyChainsByName(String name) {
        if (isInvalidSearchTerm(name)) {
            return Collections.emptyList();
        }

        return supplyChainList.stream()
                .filter(supplyChain -> matchesSupplyChainName(supplyChain, name))
                .collect(Collectors.toList());
    }

    /**
     * Finds products by category or type and returns defensive copies of matches.
     *
     * @param category the category to filter by
     * @return a list of copies of products matching the category; empty list if category invalid
     */
    public List<Product> findProductsByCategory(String category) {
        if (isInvalidSearchTerm(category)) {
            return Collections.emptyList();
        }

        return productList.stream()
                .filter(product -> matchesProductCategory(product, category))
                .map(Product::clone)
                .collect(Collectors.toList());
    }

    /**
     * Validates that a product is not null.
     *
     * @param product candidate product
     * @throws IllegalArgumentException if product is null
     */
    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
    }

    /**
     * Validates that a product is available in the system.
     * The check relies on {@code equals} which is based on product id.
     *
     * @param product candidate product
     * @throws IllegalArgumentException if product is not available
     */
    private void validateProductAvailability(Product product) {
        if (!productList.contains(product)) {
            throw new IllegalArgumentException("Product is not available in the supply chain");
        }
    }

    /**
     * Validates that the supply chain name is not null or empty.
     *
     * @param name candidate supply chain name
     * @throws IllegalArgumentException if name is null or empty
     */
    private void validateSupplyChainName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Supply chain name cannot be null or empty");
        }
    }

    /**
     * Validates that the product list is not null.
     *
     * @param products candidate products
     * @throws IllegalArgumentException if products is null
     */
    private void validateProductList(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("Product list cannot be null");
        }
    }

    /**
     * Builds a new SupplyChain instance. Products are copied to ensure defensive isolation.
     *
     * @param name     supply chain name
     * @param products source products
     * @return a new SupplyChain instance
     */
    private SupplyChain buildSupplyChain(String name, List<Product> products) {
        List<Product> copies = products.stream()
                .map(Product::clone)
                .collect(Collectors.toList());

        return new SupplyChain(name, copies);
    }

    /**
     * Checks if a search term is invalid (null or empty).
     *
     * @param term candidate search term
     * @return true if null or empty
     */
    private boolean isInvalidSearchTerm(String term) {
        return term == null || term.trim().isEmpty();
    }

    /**
     * Checks if a supply chain matches the given name pattern (case-insensitive).
     *
     * @param supplyChain the supply chain instance
     * @param name        the name pattern
     * @return true if supply chain name contains the pattern
     */
    private boolean matchesSupplyChainName(SupplyChain supplyChain, String name) {
        return supplyChain.getName()
                .toLowerCase()
                .contains(name.toLowerCase().trim());
    }

    /**
     * Checks if a product matches the given category (case-insensitive).
     *
     * @param product  the product instance
     * @param category the category to match
     * @return true if the product category contains the search term
     */
    private boolean matchesProductCategory(Product product, String category) {
        return product.getCategory()
                .toLowerCase()
                .contains(category.toLowerCase().trim());
    }
}