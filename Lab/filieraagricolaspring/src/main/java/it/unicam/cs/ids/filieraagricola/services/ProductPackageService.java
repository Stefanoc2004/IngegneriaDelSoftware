package it.unicam.cs.ids.filieraagricola.services;


import it.unicam.cs.ids.filieraagricola.controllers.dto.ProductPackageDTO;
import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.ProductPackage;
import it.unicam.cs.ids.filieraagricola.model.repositories.ProductPackageRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service for managing {@link ProductPackage} aggregates.
 *
 * <p>Provides CRUD-like operations backed by Spring Data repositories. The
 * service assembles packages from referenced {@link Product} entities.</p>
 */
@Service
public class ProductPackageService {


    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPackageRepository productPackageRepository;


    /** Returns all product packages. */
    public List<ProductPackage> findAll() {
        return productPackageRepository.findAll();
    }


    /** Returns a package by id or null if not found. */
    public ProductPackage findById(String id) {
        Optional<ProductPackage> opt = productPackageRepository.findById(id);
        if (opt.isEmpty()) {
            return null;
        }
        return opt.get();
    }

    /** Deletes a package by id if present. */
    public boolean delete(String id) {
        Optional<ProductPackage> opt = productPackageRepository.findById(id);
        if (opt.isEmpty()) {
            return false;
        }
        productPackageRepository.delete(opt.get());
        return true;
    }

    /**
     * Creates a package from the given DTO by resolving product ids.
     */
    public ProductPackage create(ProductPackageDTO dto) {
        List<Product> products = new LinkedList<>();
        for (String productId : dto.getProductsId()) {
            Optional<Product> otp = productRepository.findById(productId);
            if (otp.isPresent()) {
                products.add(otp.get());
            }
        }
        ProductPackage productPackage = new ProductPackage();
        productPackage.setName(dto.getName());
        productPackage.setProducts(products);
        productPackage.setId(UUID.randomUUID().toString());
        return productPackageRepository.save(productPackage);

    }
}
