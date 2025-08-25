package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for managing product packages in the agricultural supply chain.
 * This service handles package creation, modification, and retrieval operations
 * following the Single Responsibility Principle.
 */
public class PackageService {

    private final List<ProductPackage> packageList;

    /**
     * Constructs a new PackageService with an empty package list.
     */
    public PackageService() {
        this.packageList = new ArrayList<>();
    }

    /**
     * Creates a new product package and adds it to the managed collection.
     *
     * @param packageName the name of the package, must not be null or empty
     * @param products the list of products to include in the package
     * @return the created ProductPackage instance
     * @throws IllegalArgumentException if package name is null or empty
     */
    public ProductPackage createPackage(String packageName, List<Product> products) {
        validatePackageName(packageName);
        validateProductList(products);

        var packageItem = buildPackage(packageName, products);
        packageList.add(packageItem);
        return packageItem;
    }

    /**
     * Retrieves all packages managed by this service.
     *
     * @return an unmodifiable list of all packages
     */
    public List<ProductPackage> getPackageList() {
        return Collections.unmodifiableList(packageList);
    }

    /**
     * Finds packages by name using case-insensitive matching.
     *
     * @param name the name to search for
     * @return a list of packages matching the given name
     */
    public List<ProductPackage> findPackagesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return packageList.stream()
                .filter(packageItem -> matchesName(packageItem, name))
                .collect(Collectors.toList());
    }

    /**
     * Removes a package from the managed collection.
     *
     * @param packageItem the package to remove
     * @return true if the package was removed, false otherwise
     */
    public boolean removePackage(ProductPackage packageItem) {
        return packageList.remove(packageItem);
    }

    /**
     * Updates an existing package with new product list.
     *
     * @param packageItem the package to update
     * @param newProducts the new list of products
     * @throws IllegalArgumentException if package is not managed by this service
     */
    public void updatePackage(ProductPackage packageItem, List<Product> newProducts) {
        validateManagedPackage(packageItem);
        validateProductList(newProducts);
    }

    /**
     * Validates that the package name is not null or empty.
     */
    private void validatePackageName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Package name cannot be null or empty");
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
     * Builds a new package with the given parameters.
     */
    private ProductPackage buildPackage(String name, List<Product> products) {
        return new ProductPackage(name, products);
    }

    /**
     * Checks if a package matches the given name (case-insensitive).
     */
    private boolean matchesName(ProductPackage packageItem, String name) {
        return packageItem.getName()
                .toLowerCase()
                .contains(name.toLowerCase().trim());
    }

    /**
     * Validates that the package is managed by this service.
     */
    private void validateManagedPackage(ProductPackage packageItem) {
        if (!packageList.contains(packageItem)) {
            throw new IllegalArgumentException("Package is not managed by this service");
        }
    }
}