package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for managing the agricultural supply chain (filiera).
 * Handles supply chain actors, products, and their relationships within the system.
 * Follows Single Responsibility Principle by focusing solely on supply chain management.
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
     * Acquires a product and adds it to the managed product collection.
     * This method represents the acquisition process in the supply chain.
     *
     * @param product the product to acquire, must not be null
     * @return the acquired product
     * @throws IllegalArgumentException if product is null
     */
    public Product acquireProduct(Product product) {
        validateProduct(product);

        if (!productList.contains(product)) {
            productList.add(product);
        }

        return product;
    }

    /**
     * Sells a product by removing it from the managed collection.
     *
     * @param product the product to sell
     * @return true if the product was successfully sold (removed)
     * @throws IllegalArgumentException if product is null or not available
     */
    public boolean sellProduct(Product product) {
        validateProduct(product);
        validateProductAvailability(product);

        return productList.remove(product);
    }

    /**
     * Retrieves all products currently in the supply chain.
     *
     * @return an unmodifiable list of all products
     */
    public List<Product> getProductList() {
        return Collections.unmodifiableList(productList);
    }

    /**
     * Retrieves all supply chains managed by this service.
     *
     * @return an unmodifiable list of all supply chains
     */
    public List<SupplyChain> getSupplyChainList() {
        return Collections.unmodifiableList(supplyChainList);
    }

    /**
     * Creates a new supply chain and adds it to the managed collection.
     *
     * @param supplyChainName the name of the supply chain
     * @param products the products associated with this supply chain
     * @return the created SupplyChain instance
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
     * @return a list of matching supply chains
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
     * Finds products by category or type.
     *
     * @param category the category to filter by
     * @return a list of products matching the category
     */
    public List<Product> findProductsByCategory(String category) {
        if (isInvalidSearchTerm(category)) {
            return Collections.emptyList();
        }

        return productList.stream()
                .filter(product -> matchesProductCategory(product, category))
                .collect(Collectors.toList());
    }

    /**
     * Validates that a product is not null.
     */
    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
    }

    /**
     * Validates that a product is available in the system.
     */
    private void validateProductAvailability(Product product) {
        if (!productList.contains(product)) {
            throw new IllegalArgumentException("Product is not available in the supply chain");
        }
    }

    /**
     * Validates that the supply chain name is not null or empty.
     */
    private void validateSupplyChainName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Supply chain name cannot be null or empty");
        }
    }

    /**
     * Validates that the product list is not null.
     */
    private void validateProductList(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("Product list cannot be null");
        }
    }

    /**
     * Builds a new supply chain instance.
     */
    private SupplyChain buildSupplyChain(String name, List<Product> products) {
        return new SupplyChain(name, products);
    }

    /**
     * Checks if a search term is invalid (null or empty).
     */
    private boolean isInvalidSearchTerm(String term) {
        return term == null || term.trim().isEmpty();
    }

    /**
     * Checks if a supply chain matches the given name pattern.
     */
    private boolean matchesSupplyChainName(SupplyChain supplyChain, String name) {
        return supplyChain.getName()
                .toLowerCase()
                .contains(name.toLowerCase().trim());
    }

    /**
     * Checks if a product matches the given category.
     */
    private boolean matchesProductCategory(Product product, String category) {
        return product.getCategory()
                .toLowerCase()
                .contains(category.toLowerCase().trim());
    }
}
