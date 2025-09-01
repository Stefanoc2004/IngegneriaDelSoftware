package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.ProductPackage;

import java.util.*;

/**
 * Service responsible for managing product packages (bundles).
 *
 * <p>All stored and returned packages/products are handled through defensive
 * copying (cloning). Public lists are produced using {@code Stream.toList()}
 * and are therefore effectively unmodifiable for external callers.</p>
 */
public class PackageService {

    private final List<ProductPackage> packageList;

    /**
     * Constructs a new PackageService instance.
     */
    public PackageService() {
        this.packageList = new ArrayList<>();
    }

    /**
     * Creates a new product package and stores it internally.
     *
     * <p>The method clones provided products and stores a new internal {@link ProductPackage}.
     * The returned value is a clone of the stored package (so callers never get the internal instance).</p>
     *
     * @param packageName package name (must not be null or empty)
     * @param products    products to include (must not be null)
     * @return clone of the created ProductPackage
     * @throws IllegalArgumentException if inputs are invalid
     */
    public ProductPackage createPackage(String packageName, List<Product> products) {
        validatePackageName(packageName);
        validateProductList(products);

        var packageItem = buildPackage(packageName, products);
        packageList.add(packageItem);
        return packageItem.clone();
    }

    /**
     * Returns an unmodifiable list of packages (each element is a clone).
     *
     * <p>Each package in the returned list is a defensive copy to avoid exposing
     * internal mutable state.</p>
     *
     * @return list of product package clones (effectively unmodifiable)
     */
    public List<ProductPackage> getPackageList() {
        return packageList.stream()
                .map(ProductPackage::clone)
                .toList();
    }

    /**
     * Finds packages whose name contains the provided search term (case-insensitive).
     *
     * <p>Returned list contains clones of the matching packages (defensive copies).</p>
     *
     * @param name search term (may not be null/empty)
     * @return list of matching package clones (empty list if none)
     */
    public List<ProductPackage> findPackagesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String norm = name.toLowerCase().trim();
        return packageList.stream()
                .filter(p -> p.getName().toLowerCase().contains(norm))
                .map(ProductPackage::clone)
                .toList();
    }

    /**
     * Removes the specified package from the internal storage.
     *
     * @param packageItem package to remove (must not be null)
     * @return {@code true} if removed, {@code false} otherwise
     * @throws IllegalArgumentException if packageItem is null
     */
    public boolean removePackage(ProductPackage packageItem) {
        if (packageItem == null) throw new IllegalArgumentException("PackageItem cannot be null");
        return packageList.remove(packageItem);
    }

    /**
     * Updates the products of an existing managed package.
     *
     * <p>The provided product list is defensively copied (each product is cloned)
     * and then set on the managed package instance.</p>
     *
     * @param packageItem the package to update (must already be managed)
     * @param newProducts the new list of products (must not be null)
     * @throws IllegalArgumentException if package not managed or newProducts is null
     */
    public void updatePackage(ProductPackage packageItem, List<Product> newProducts) {
        validateManagedPackage(packageItem);
        validateProductList(newProducts);

        List<Product> copied = newProducts.stream()
                .map(Product::clone)
                .toList();

        int idx = packageList.indexOf(packageItem);
        ProductPackage managed = packageList.get(idx);
        managed.setProducts(copied);
    }

    /* ----------------- private helpers ----------------- */

    /**
     * Validates the package name.
     *
     * @param name candidate package name
     * @throws IllegalArgumentException if name is null or empty
     */
    private void validatePackageName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Package name cannot be null or empty");
        }
    }

    /**
     * Validates the product list argument.
     *
     * @param products candidate product list
     * @throws IllegalArgumentException if products is null
     */
    private void validateProductList(List<Product> products) {
        if (products == null) throw new IllegalArgumentException("Product list cannot be null");
    }

    /**
     * Builds a new ProductPackage instance by defensively copying the provided products.
     *
     * @param name     package name
     * @param products source products (not null)
     * @return a new ProductPackage instance containing clones of the provided products
     */
    private ProductPackage buildPackage(String name, List<Product> products) {
        List<Product> copies = products.stream()
                .map(Product::clone)
                .toList();
        return new ProductPackage(name, copies);
    }

    /**
     * Validates that the package is managed by this service.
     *
     * @param packageItem candidate package
     * @throws IllegalArgumentException if the package is null or not present in the managed collection
     */
    private void validateManagedPackage(ProductPackage packageItem) {
        if (packageItem == null || !packageList.contains(packageItem)) {
            throw new IllegalArgumentException("Package is not managed by this service");
        }
    }
}