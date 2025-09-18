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

@Service
public class ProductPackageService {


    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPackageRepository productPackageRepository;


    public List<ProductPackage> findAll() {
        return productPackageRepository.findAll();
    }


    public ProductPackage findById(String id) {
        Optional<ProductPackage> opt = productPackageRepository.findById(id);
        if (opt.isEmpty()) {
            return null;
        }
        return opt.get();
    }

    public boolean delete(String id) {
        Optional<ProductPackage> opt = productPackageRepository.findById(id);
        if (opt.isEmpty()) {
            return false;
        }
        productPackageRepository.delete(opt.get());
        return true;
    }

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
