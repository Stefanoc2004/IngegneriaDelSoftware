package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.SupplyChain;
import it.unicam.cs.ids.filieraagricola.service.exception.NotFoundException;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for managing supply chains and their products.
 *
 * <p>This service encapsulates all business logic related to supply chains,
 * including validation, product filtering, activity checks, and supply chain
 * analysis. It follows the Single Responsibility Principle by handling all
 * supply chain operations while the SupplyChain model focuses solely on data.</p>
 *
 * <p>The service maintains repositories for both supply chains and products,
 * ensuring data integrity through defensive copying and proper validation.</p>
 *
 * @author Agricultural Platform Team
 * @version 2.0
 */
public class SupplyChainService {

    private final List<SupplyChain> supplyChainRepository;
    private final List<Product> productRepository;
    private final ProductService productService;

    /**
     * Constructs a new SupplyChainService.
     */
    public SupplyChainService() {
        this.supplyChainRepository = new ArrayList<>();
        this.productRepository = new ArrayList<>();
        this.productService = new ProductService();
    }

    /**
     * Constructs a SupplyChainService with a product service.
     *
     * @param productService the product service to use
     */
    public SupplyChainService(ProductService productService) {
        this.supplyChainRepository = new ArrayList<>();
        this.productRepository = new ArrayList<>();
        this.productService = productService;
    }

    /**
     * Creates a new supply chain with validation.
     *
     * @param name            supply chain name
     * @param description     supply chain description
     * @param products        initial products
     * @param territorialArea territorial area
     * @return created supply chain (defensive copy)
     * @throws ValidationException if validation fails
     */
    public SupplyChain createSupplyChain(String name,
                                         String description,
                                         List<Product> products,
                                         String territorialArea) {

        validateSupplyChainData(name, description, territorialArea);

        String id = generateSupplyChainId(name, territorialArea);

        SupplyChain supplyChain = new SupplyChain(
                id,
                normalizeString(name),
                normalizeString(description),
                products != null ? new ArrayList<>(products) : new ArrayList<>(),
                LocalDateTime.now(),
                normalizeString(territorialArea)
        );

        supplyChainRepository.add(supplyChain);
        return new SupplyChain(supplyChain);
    }

    /**
     * Creates a simple supply chain with name and products.
     *
     * @param supplyChainName supply chain name
     * @param products        products to include
     * @return created supply chain (defensive copy)
     * @throws ValidationException if validation fails
     */
    public SupplyChain createSupplyChain(String supplyChainName,
                                         List<Product> products) {
        return createSupplyChain(
                supplyChainName,
                "Supply chain for " + supplyChainName,
                products,
                "default"
        );
    }

    /**
     * Finds a supply chain by ID.
     *
     * @param chainId the supply chain ID
     * @return defensive copy of the found supply chain
     * @throws NotFoundException if not found
     */
    public SupplyChain findById(String chainId) {
        return supplyChainRepository.stream()
                .filter(sc -> sc.getId().equals(chainId))
                .findFirst()
                .map(SupplyChain::new)
                .orElseThrow(() -> new NotFoundException(
                        "Supply chain not found with ID: " + chainId));
    }

    /**
     * Finds supply chains by name pattern.
     *
     * @param namePattern search pattern (case-insensitive)
     * @return list of matching supply chains (defensive copies)
     */
    public List<SupplyChain> findSupplyChainsByName(String namePattern) {
        if (isNullOrEmpty(namePattern)) {
            return Collections.emptyList();
        }

        String normalized = namePattern.toLowerCase().trim();
        return supplyChainRepository.stream()
                .filter(sc -> sc.getName() != null &&
                        sc.getName().toLowerCase().contains(normalized))
                .map(SupplyChain::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets organic products from a supply chain.
     *
     * @param chainId the supply chain ID
     * @return list of organic products (defensive copies)
     * @throws NotFoundException if chain not found
     */
    public List<Product> getOrganicProducts(String chainId) {
        SupplyChain chain = findByIdInternal(chainId);

        return chain.getProducts().stream()
                .filter(p -> p != null && productService.isOrganic(p))
                .map(Product::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets certified products from a supply chain.
     *
     * @param chainId the supply chain ID
     * @return list of certified products (defensive copies)
     * @throws NotFoundException if chain not found
     */
    public List<Product> getCertifiedProducts(String chainId) {
        SupplyChain chain = findByIdInternal(chainId);

        return chain.getProducts().stream()
                .filter(p -> p != null && productService.hasCertifications(p))
                .map(Product::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets products by category from a supply chain.
     *
     * @param chainId  the supply chain ID
     * @param category the category to filter by
     * @return list of matching products (defensive copies)
     * @throws NotFoundException if chain not found
     */
    public List<Product> getProductsByCategory(String chainId,
                                               String category) {
        if (isNullOrEmpty(category)) {
            return Collections.emptyList();
        }

        SupplyChain chain = findByIdInternal(chainId);
        String normalized = category.toLowerCase().trim();

        return chain.getProducts().stream()
                .filter(p -> p != null &&
                        p.getCategory() != null &&
                        p.getCategory().toLowerCase().equals(normalized))
                .map(Product::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets fresh products from a supply chain.
     *
     * @param chainId the supply chain ID
     * @return list of fresh products (defensive copies)
     * @throws NotFoundException if chain not found
     */
    public List<Product> getFreshProducts(String chainId) {
        SupplyChain chain = findByIdInternal(chainId);

        return chain.getProducts().stream()
                .filter(p -> p != null && productService.isFresh(p))
                .map(Product::new)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a supply chain is active.
     *
     * <p>A chain is active if it has products and was created
     * within the last 6 months.</p>
     *
     * @param chainId the supply chain ID
     * @return true if active, false otherwise
     * @throws NotFoundException if chain not found
     */
    public boolean isActive(String chainId) {
        SupplyChain chain = findByIdInternal(chainId);

        return chain.getProducts() != null &&
                !chain.getProducts().isEmpty() &&
                chain.getCreationDate() != null &&
                chain.getCreationDate().isAfter(
                        LocalDateTime.now().minusMonths(6));
    }

    /**
     * Gets unique product categories in a supply chain.
     *
     * @param chainId the supply chain ID
     * @return set of unique categories
     * @throws NotFoundException if chain not found
     */
    public Set<String> getUniqueCategories(String chainId) {
        SupplyChain chain = findByIdInternal(chainId);

        return chain.getProducts().stream()
                .map(Product::getCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Adds a product to the supply chain and repository.
     *
     * @param product product to add
     * @return defensive copy of the added product
     * @throws ValidationException if product is null
     */
    public Product acquireProduct(Product product) {
        if (product == null) {
            throw new ValidationException("Product cannot be null");
        }

        Product copy = new Product(product);
        if (!productRepository.contains(product)) {
            productRepository.add(copy);
        }

        return new Product(copy);
    }

    /**
     * Removes a product from the repository.
     *
     * @param product product to remove
     * @return true if removed, false otherwise
     * @throws ValidationException if product is null or not available
     */
    public boolean sellProduct(Product product) {
        if (product == null) {
            throw new ValidationException("Product cannot be null");
        }

        if (!productRepository.contains(product)) {
            throw new ValidationException(
                    "Product is not available in the supply chain");
        }

        return productRepository.remove(product);
    }

    /**
     * Adds products to an existing supply chain.
     *
     * @param chainId  the supply chain ID
     * @param products products to add
     * @return updated supply chain (defensive copy)
     * @throws NotFoundException if chain not found
     */
    public SupplyChain addProductsToChain(String chainId,
                                          List<Product> products) {
        SupplyChain chain = findByIdInternal(chainId);

        if (products != null) {
            products.forEach(p -> {
                if (p != null) {
                    chain.getProducts().add(new Product(p));
                }
            });
        }

        return new SupplyChain(chain);
    }

    /**
     * Removes a product from a supply chain.
     *
     * @param chainId   the supply chain ID
     * @param productId the product ID to remove
     * @return updated supply chain (defensive copy)
     * @throws NotFoundException if chain or product not found
     */
    public SupplyChain removeProductFromChain(String chainId,
                                              String productId) {
        SupplyChain chain = findByIdInternal(chainId);

        boolean removed = chain.getProducts()
                .removeIf(p -> p.getId().equals(productId));

        if (!removed) {
            throw new NotFoundException(
                    "Product not found in supply chain: " + productId);
        }

        return new SupplyChain(chain);
    }

    /**
     * Gets all managed products.
     *
     * @return unmodifiable list of product copies
     */
    public List<Product> getProductList() {
        return Collections.unmodifiableList(
                productRepository.stream()
                        .map(Product::new)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Gets all supply chains.
     *
     * @return unmodifiable list of supply chain copies
     */
    public List<SupplyChain> getSupplyChainList() {
        return Collections.unmodifiableList(
                supplyChainRepository.stream()
                        .map(SupplyChain::new)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Finds products by category across all chains.
     *
     * @param category category to search for
     * @return list of matching products (defensive copies)
     */
    public List<Product> findProductsByCategory(String category) {
        if (isNullOrEmpty(category)) {
            return Collections.emptyList();
        }

        String normalized = category.toLowerCase().trim();
        return productRepository.stream()
                .filter(p -> p.getCategory() != null &&
                        p.getCategory().toLowerCase().contains(normalized))
                .map(Product::new)
                .collect(Collectors.toList());
    }

    /**
     * Validates supply chain for completeness.
     *
     * @param chainId the supply chain ID
     * @throws NotFoundException if chain not found
     * @throws ValidationException if chain is incomplete
     */
    public void validateSupplyChain(String chainId) {
        SupplyChain chain = findByIdInternal(chainId);

        if (isNullOrEmpty(chain.getName())) {
            throw new ValidationException(
                    "Supply chain must have a name");
        }

        if (isNullOrEmpty(chain.getTerritorialArea())) {
            throw new ValidationException(
                    "Supply chain must have a territorial area");
        }
    }

    /* ----------------- Helper Methods ----------------- */

    /**
     * Internal method to find chain without defensive copy.
     *
     * @param chainId the chain ID
     * @return actual chain instance
     * @throws NotFoundException if not found
     */
    private SupplyChain findByIdInternal(String chainId) {
        return supplyChainRepository.stream()
                .filter(sc -> sc.getId().equals(chainId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        "Supply chain not found with ID: " + chainId));
    }

    /**
     * Validates supply chain data.
     *
     * @param name            chain name
     * @param description     chain description
     * @param territorialArea territorial area
     * @throws ValidationException if validation fails
     */
    private void validateSupplyChainData(String name,
                                         String description,
                                         String territorialArea) {
        if (isNullOrEmpty(name)) {
            throw new ValidationException(
                    "Supply chain name cannot be null or empty");
        }

        if (isNullOrEmpty(description)) {
            throw new ValidationException(
                    "Supply chain description cannot be null or empty");
        }

        if (isNullOrEmpty(territorialArea)) {
            throw new ValidationException(
                    "Territorial area cannot be null or empty");
        }
    }

    /**
     * Generates a unique supply chain ID.
     *
     * @param name supply chain name
     * @param area territorial area
     * @return generated ID
     */
    private String generateSupplyChainId(String name, String area) {
        return (name + "_" + area + "_" + System.currentTimeMillis())
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param str string to check
     * @return true if null or empty
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Normalizes a string by trimming whitespace.
     *
     * @param str string to normalize
     * @return normalized string
     */
    private String normalizeString(String str) {
        return str == null ? "" : str.trim();
    }
}