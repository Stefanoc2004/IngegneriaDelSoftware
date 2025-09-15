package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.ProductPackage;
import it.unicam.cs.ids.filieraagricola.service.exception.NotFoundException;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for managing product packages (bundles).
 *
 * <p>This service encapsulates all business logic related to product packages,
 * including validation, creation, modification, and querying. It follows the
 * Single Responsibility Principle by handling all package-related operations
 * while the ProductPackage model focuses solely on data representation.</p>
 *
 * <p>All operations ensure data integrity through defensive copying where
 * necessary, preventing external modifications to internal state.</p>
 *
 * @author Agricultural Platform Team
 * @version 2.0
 */
public class PackageService {

    private final List<ProductPackage> packageRepository;

    /**
     * Constructs a new PackageService instance.
     */
    public PackageService() {
        this.packageRepository = new ArrayList<>();
    }

    /**
     * Creates a new product package with validation.
     *
     * <p>Validates the package name and products list before creating
     * the package. Returns a defensive copy of the created package.</p>
     *
     * @param packageName package name (must not be null or empty)
     * @param products    products to include (must not be null)
     * @return copy of the created ProductPackage
     * @throws ValidationException if inputs are invalid
     */
    public ProductPackage createPackage(String packageName,
                                        List<Product> products) {
        validatePackageName(packageName);
        validateProductList(products);

        ProductPackage newPackage = new ProductPackage(
                normalizeString(packageName),
                createProductsCopy(products)
        );

        packageRepository.add(newPackage);
        return new ProductPackage(newPackage);
    }

    /**
     * Adds a product to an existing package.
     *
     * @param packageId the ID of the package
     * @param product   the product to add
     * @return the updated package (defensive copy)
     * @throws NotFoundException if package not found
     * @throws ValidationException if product is null
     */
    public ProductPackage addProductToPackage(String packageId,
                                              Product product) {
        if (product == null) {
            throw new ValidationException("Product cannot be null");
        }

        // get actual instance to modify
        ProductPackage pkg = findPackageByIdInternal(packageId);
        pkg.getProducts().add(new Product(product));

        // return defensive copy
        return new ProductPackage(pkg);
    }

    /**
     * Removes a product from a package.
     *
     * @param packageId the ID of the package
     * @param productId the ID of the product to remove
     * @return the updated package (defensive copy)
     * @throws NotFoundException if package or product not found
     */
    public ProductPackage removeProductFromPackage(String packageId,
                                                   String productId) {
        // modify actual instance
        ProductPackage pkg = findPackageByIdInternal(packageId);

        boolean removed = pkg.getProducts().removeIf(p -> p.getId().equals(productId));

        if (!removed) {
            throw new NotFoundException("Product not found in package: " + productId);
        }

        return new ProductPackage(pkg);
    }

    /**
     * Updates the products of an existing package.
     *
     * <p>Replaces all products in the package with the new list.
     * Creates defensive copies of all products.</p>
     *
     * @param packageId   the ID of the package to update
     * @param newProducts the new list of products
     * @return the updated package (defensive copy)
     * @throws NotFoundException if package not found
     * @throws ValidationException if newProducts is null
     */
    public ProductPackage updatePackageProducts(String packageId,
                                                List<Product> newProducts) {
        validateProductList(newProducts);

        // modify actual instance
        ProductPackage pkg = findPackageByIdInternal(packageId);
        pkg.setProducts(createProductsCopy(newProducts));

        return new ProductPackage(pkg);
    }

    /**
     * Updates the name of an existing package.
     *
     * @param packageId the ID of the package
     * @param newName   the new name
     * @return the updated package (defensive copy)
     * @throws NotFoundException if package not found
     * @throws ValidationException if name is invalid
     */
    public ProductPackage updatePackageName(String packageId,
                                            String newName) {
        validatePackageName(newName);

        // modify actual instance
        ProductPackage pkg = findPackageByIdInternal(packageId);
        pkg.setName(normalizeString(newName));

        return new ProductPackage(pkg);
    }

    /**
     * Finds a package by its ID.
     *
     * @param packageId the package ID
     * @return defensive copy of the found package
     * @throws NotFoundException if package not found
     */
    public ProductPackage findPackageById(String packageId) {
        return packageRepository.stream()
                .filter(p -> p.getId().equals(packageId))
                .findFirst()
                .map(ProductPackage::new)
                .orElseThrow(() -> new NotFoundException(
                        "Package not found with ID: " + packageId));
    }

    /**
     * Internal method to find package without defensive copy.
     *
     * @param packageId the package ID
     * @return the actual package instance (not a copy)
     * @throws NotFoundException if package not found
     */
    private ProductPackage findPackageByIdInternal(String packageId) {
        return packageRepository.stream()
                .filter(p -> p.getId().equals(packageId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        "Package not found with ID: " + packageId));
    }

    /**
     * Finds packages whose name contains the search term.
     *
     * <p>Search is case-insensitive. Returns defensive copies.</p>
     *
     * @param searchTerm search term
     * @return list of matching packages (defensive copies)
     */
    public List<ProductPackage> findPackagesByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String normalized = searchTerm.toLowerCase().trim();
        return packageRepository.stream()
                .filter(p -> p.getName() != null &&
                        p.getName().toLowerCase().contains(normalized))
                .map(ProductPackage::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets all packages.
     *
     * @return unmodifiable list of all packages (defensive copies)
     */
    public List<ProductPackage> getAllPackages() {
        return Collections.unmodifiableList(
                packageRepository.stream()
                        .map(ProductPackage::new)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Removes a package by ID.
     *
     * @param packageId the ID of the package to remove
     * @return true if removed, false otherwise
     * @throws NotFoundException if package not found
     */
    public boolean removePackage(String packageId) {
        ProductPackage pkg = findPackageByIdInternal(packageId);
        return packageRepository.remove(pkg);
    }

    /**
     * Gets the product count for a specific package.
     *
     * @param packageId the package ID
     * @return number of products in the package
     * @throws NotFoundException if package not found
     */
    public int getProductCount(String packageId) {
        ProductPackage pkg = findPackageByIdInternal(packageId);
        return pkg.getProducts() != null ? pkg.getProducts().size() : 0;
    }

    /**
     * Checks if a package contains a specific product.
     *
     * @param packageId the package ID
     * @param productId the product ID to check
     * @return true if the package contains the product
     * @throws NotFoundException if package not found
     */
    public boolean packageContainsProduct(String packageId,
                                          String productId) {
        ProductPackage pkg = findPackageByIdInternal(packageId);
        return pkg.getProducts().stream()
                .anyMatch(p -> p.getId().equals(productId));
    }

    /**
     * Validates package can be created or modified.
     *
     * <p>Checks that package has a name and at least one product.</p>
     *
     * @param pkg the package to validate
     * @throws ValidationException if validation fails
     */
    public void validatePackage(ProductPackage pkg) {
        if (pkg == null) {
            throw new ValidationException("Package cannot be null");
        }

        validatePackageName(pkg.getName());

        if (pkg.getProducts() == null || pkg.getProducts().isEmpty()) {
            throw new ValidationException(
                    "Package must contain at least one product");
        }
    }

    /**
     * Calculates total value of a package.
     *
     * <p>This is a placeholder for future pricing logic.</p>
     *
     * @param packageId the package ID
     * @return calculated value (currently returns product count)
     */
    public double calculatePackageValue(String packageId) {
        // Placeholder for future pricing implementation
        return getProductCount(packageId);
    }

    /* ----------------- Helper Methods ----------------- */

    /**
     * Validates the package name.
     *
     * @param name candidate package name
     * @throws ValidationException if name is null or empty
     */
    private void validatePackageName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException(
                    "Package name cannot be null or empty");
        }
    }

    /**
     * Validates the product list.
     *
     * @param products candidate product list
     * @throws ValidationException if products is null
     */
    private void validateProductList(List<Product> products) {
        if (products == null) {
            throw new ValidationException(
                    "Product list cannot be null");
        }
    }

    /**
     * Creates defensive copies of products.
     *
     * @param products source products
     * @return list with copied products
     */
    private List<Product> createProductsCopy(List<Product> products) {
        if (products == null) {
            return new ArrayList<>();
        }

        return products.stream()
                .map(p -> p == null ? null : new Product(p))
                .collect(Collectors.toList());
    }

    /**
     * Normalizes a string by trimming whitespace.
     *
     * @param str the string to normalize
     * @return normalized string
     */
    private String normalizeString(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * Gets the total number of packages.
     *
     * @return count of all packages
     */
    public int getPackageCount() {
        return packageRepository.size();
    }
}