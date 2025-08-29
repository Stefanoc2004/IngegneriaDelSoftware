package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.ProductPackage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for managing product packages in the agricultural supply chain.
 *
 * <p>This service applies defensive copying using the Prototype pattern: every Product
 * stored internally is a copy created via {@code Product.clone()} to avoid accidental
 * external mutations. Public collections returned by this service are unmodifiable.</p>
 *
 * <p>All public and important private methods are documented in English.</p>
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
     * Each product is defensively copied before being stored.
     *
     * @param packageName the name of the package, must not be null or empty
     * @param products    the list of products to include in the package (must not be null)
     * @return the created ProductPackage instance (internal instance, do not modify)
     * @throws IllegalArgumentException if packageName is invalid or products is null
     */
    public ProductPackage createPackage(String packageName, List<Product> products) {
        validatePackageName(packageName);
        validateProductList(products);

        var packageItem = buildPackage(packageName, products);
        packageList.add(packageItem);
        return packageItem;
    }

    /**
     * Returns an unmodifiable view of the packages managed by this service.
     * The returned list is a read-only view of the internal collection.
     *
     * @return unmodifiable list of ProductPackage
     */
    public List<ProductPackage> getPackageList() {
        return Collections.unmodifiableList(packageList);
    }

    /**
     * Finds packages by name using case-insensitive matching.
     *
     * @param name the name to search for
     * @return a list of packages matching the given name; empty list if name is null/empty
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
     * Updates an existing package with a new product list.
     * The supplied products are defensively copied and set into the managed package.
     *
     * @param packageItem the package to update (must be already managed by this service)
     * @param newProducts the new list of products (must not be null)
     * @throws IllegalArgumentException if package is not managed by this service or newProducts is null
     */
    public void updatePackage(ProductPackage packageItem, List<Product> newProducts) {
        validateManagedPackage(packageItem);
        validateProductList(newProducts);

        // Defensive copy of each Product using Prototype
        List<Product> copied = newProducts.stream()
                .map(Product::clone)
                .collect(Collectors.toList());

        packageItem.setProducts(copied);
    }

    /**
     * Validates that the package name is not null or empty.
     *
     * @param name package name candidate
     * @throws IllegalArgumentException if name is null or empty
     */
    private void validatePackageName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Package name cannot be null or empty");
        }
    }

    /**
     * Validates that the product list is not null.
     *
     * @param products product list candidate
     * @throws IllegalArgumentException if products is null
     */
    private void validateProductList(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("Product list cannot be null");
        }
    }

    /**
     * Builds a new ProductPackage. Products passed to the package are defensively copied
     * using the {@code Product.copy()} method to avoid sharing mutable references.
     *
     * @param name     package name
     * @param products source products (not null)
     * @return a new ProductPackage instance containing copies of the provided products
     */
    private ProductPackage buildPackage(String name, List<Product> products) {
        List<Product> copies = products.stream()
                .map(Product::clone)
                .collect(Collectors.toList());
        return new ProductPackage(name, copies);
    }

    /**
     * Checks if a package matches the given name (case-insensitive).
     *
     * @param packageItem the package to check
     * @param name        the search name
     * @return true if the package name contains the search term (case-insensitive)
     */
    private boolean matchesName(ProductPackage packageItem, String name) {
        return packageItem.getName()
                .toLowerCase()
                .contains(name.toLowerCase().trim());
    }

    /**
     * Validates that the package is managed by this service.
     *
     * @param packageItem candidate package
     * @throws IllegalArgumentException if the package is not present in the managed collection
     */
    private void validateManagedPackage(ProductPackage packageItem) {
        if (!packageList.contains(packageItem)) {
            throw new IllegalArgumentException("Package is not managed by this service");
        }
    }
}