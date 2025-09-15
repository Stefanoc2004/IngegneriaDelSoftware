package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;
import it.unicam.cs.ids.filieraagricola.service.exception.NotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for product business logic and validation.
 *
 * <p>This service encapsulates all business rules and operations related to
 * products, following the Single Responsibility Principle. It handles product
 * validation, quality checks, and product management operations.</p>
 *
 * @author Agricultural Platform Team
 * @version 1.0
 */
public class ProductService {

    private final List<Product> productRepository;

    /**
     * Constructs a new ProductService instance.
     */
    public ProductService() {
        this.productRepository = new ArrayList<>();
    }

    /**
     * Creates and validates a new product.
     *
     * @param name              product name
     * @param category          product category
     * @param description       product description
     * @param cultivationMethod cultivation method
     * @param certifications    certifications (optional)
     * @param productionDate    production date
     * @param producerId        producer identifier
     * @return the created product
     * @throws ValidationException if validation fails
     */
    public Product createProduct(String name,
                                 String category,
                                 String description,
                                 String cultivationMethod,
                                 String certifications,
                                 LocalDate productionDate,
                                 String producerId) {

        validateProductData(name, category, description,
                cultivationMethod, productionDate, producerId);

        Product product = new Product(
                null, // ID will be auto-generated
                normalizeString(name),
                normalizeCategory(category),
                normalizeString(description),
                normalizeCultivationMethod(cultivationMethod),
                certifications == null ? "" : certifications.trim(),
                productionDate,
                normalizeString(producerId)
        );

        productRepository.add(product);
        return product;
    }

    /**
     * Validates product data before creation or update.
     *
     * @param name              product name
     * @param category          product category
     * @param description       product description
     * @param cultivationMethod cultivation method
     * @param productionDate    production date
     * @param producerId        producer ID
     * @throws ValidationException if any validation fails
     */
    public void validateProductData(String name,
                                    String category,
                                    String description,
                                    String cultivationMethod,
                                    LocalDate productionDate,
                                    String producerId) {

        if (isNullOrEmpty(name)) {
            throw new ValidationException("Product name cannot be null or empty");
        }
        if (isNullOrEmpty(category)) {
            throw new ValidationException("Product category cannot be null or empty");
        }
        if (isNullOrEmpty(description)) {
            throw new ValidationException("Product description cannot be null or empty");
        }
        if (isNullOrEmpty(cultivationMethod)) {
            throw new ValidationException("Cultivation method cannot be null or empty");
        }
        if (productionDate == null) {
            throw new ValidationException("Production date cannot be null");
        }
        if (productionDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Production date cannot be in the future");
        }
        if (isNullOrEmpty(producerId)) {
            throw new ValidationException("Producer ID cannot be null or empty");
        }
    }

    /**
     * Checks if a product is organic based on cultivation method.
     *
     * <p>A product is considered organic if its cultivation method
     * contains "organic" or "biologico" (case-insensitive).</p>
     *
     * @param product the product to check
     * @return true if the product is organic
     */
    public boolean isOrganic(Product product) {
        if (product == null || product.getCultivationMethod() == null) {
            return false;
        }

        String method = product.getCultivationMethod().toLowerCase();
        return method.contains("organic") || method.contains("biologico");
    }

    /**
     * Checks if a product has certifications.
     *
     * @param product the product to check
     * @return true if the product has certifications
     */
    public boolean hasCertifications(Product product) {
        return product != null &&
                product.getCertifications() != null &&
                !product.getCertifications().trim().isEmpty();
    }

    /**
     * Checks if a product is fresh (produced within last 30 days).
     *
     * @param product the product to check
     * @return true if the product is fresh
     */
    public boolean isFresh(Product product) {
        if (product == null || product.getProductionDate() == null) {
            return false;
        }

        return product.getProductionDate()
                .isAfter(LocalDate.now().minusDays(30));
    }

    /**
     * Finds a product by its ID.
     *
     * @param productId the product ID to search for
     * @return the found product
     * @throws NotFoundException if product not found
     */
    public Product findById(String productId) {
        return productRepository.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        "Product not found with ID: " + productId));
    }

    /**
     * Finds all products in a specific category.
     *
     * @param category the category to filter by
     * @return list of products in the category
     */
    public List<Product> findByCategory(String category) {
        if (isNullOrEmpty(category)) {
            return Collections.emptyList();
        }

        String normalized = normalizeCategory(category);
        return productRepository.stream()
                .filter(p -> normalized.equals(
                        normalizeCategory(p.getCategory())))
                .collect(Collectors.toList());
    }

    /**
     * Finds all organic products.
     *
     * @return list of organic products
     */
    public List<Product> findOrganicProducts() {
        return productRepository.stream()
                .filter(this::isOrganic)
                .collect(Collectors.toList());
    }

    /**
     * Finds all certified products.
     *
     * @return list of certified products
     */
    public List<Product> findCertifiedProducts() {
        return productRepository.stream()
                .filter(this::hasCertifications)
                .collect(Collectors.toList());
    }

    /**
     * Finds all fresh products.
     *
     * @return list of fresh products
     */
    public List<Product> findFreshProducts() {
        return productRepository.stream()
                .filter(this::isFresh)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing product.
     *
     * @param productId the ID of the product to update
     * @param updatedProduct the updated product data
     * @return the updated product
     * @throws NotFoundException if product not found
     * @throws ValidationException if validation fails
     */
    public Product updateProduct(String productId, Product updatedProduct) {
        Product existing = findById(productId);

        validateProductData(
                updatedProduct.getName(),
                updatedProduct.getCategory(),
                updatedProduct.getDescription(),
                updatedProduct.getCultivationMethod(),
                updatedProduct.getProductionDate(),
                updatedProduct.getProducerId()
        );

        existing.setName(normalizeString(updatedProduct.getName()));
        existing.setCategory(normalizeCategory(updatedProduct.getCategory()));
        existing.setDescription(normalizeString(updatedProduct.getDescription()));
        existing.setCultivationMethod(
                normalizeCultivationMethod(updatedProduct.getCultivationMethod()));
        existing.setCertifications(updatedProduct.getCertifications());
        existing.setProductionDate(updatedProduct.getProductionDate());
        existing.setProducerId(normalizeString(updatedProduct.getProducerId()));

        return existing;
    }

    /**
     * Deletes a product by ID.
     *
     * @param productId the ID of the product to delete
     * @throws NotFoundException if product not found
     */
    public void deleteProduct(String productId) {
        Product product = findById(productId);
        productRepository.remove(product);
    }

    /**
     * Gets all products.
     *
     * @return unmodifiable list of all products
     */
    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(
                new ArrayList<>(productRepository));
    }

    /**
     * Gets the count of all products.
     *
     * @return total number of products
     */
    public int getProductCount() {
        return productRepository.size();
    }

    /* ----------------- Helper Methods ----------------- */

    /**
     * Checks if a string is null or empty.
     *
     * @param str the string to check
     * @return true if null or empty
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Normalizes a general string by trimming whitespace.
     *
     * @param str the string to normalize
     * @return normalized string
     */
    private String normalizeString(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * Normalizes a category string to lowercase.
     *
     * @param category the category to normalize
     * @return normalized category
     */
    private String normalizeCategory(String category) {
        return category == null ? "" : category.toLowerCase().trim();
    }

    /**
     * Normalizes cultivation method to lowercase.
     *
     * @param method the method to normalize
     * @return normalized method
     */
    private String normalizeCultivationMethod(String method) {
        return method == null ? "" : method.toLowerCase().trim();
    }
}