package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.SupplyChain;
import it.unicam.cs.ids.filieraagricola.model.SupplyChainPoint;
import it.unicam.cs.ids.filieraagricola.model.repositories.ProductRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.SupplyChainPointRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.SupplyChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Application service responsible for managing {@link SupplyChain}, products and points.
 *
 * <p>Provides basic create/delete/find operations relying on Spring Data repositories.
 * Current implementation does not perform defensive copying beyond what entities expose.</p>
 */
@Service
public class SupplyChainService {

    @Autowired
    private SupplyChainRepository supplyChainRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplyChainPointRepository supplyChainPointRepository;

    /**
     * Persists a new product and adds it to the specified supply chain.
     */
    public Product acquireProduct(Product product, String supplyChainId) {
        Optional<SupplyChain> opt = supplyChainRepository.findById(supplyChainId);
        if (opt.isEmpty()) {
            return null;
        }
        SupplyChain supplyChain = opt.get();
        // we create a new product with a new id
        product.setId(UUID.randomUUID().toString());
        String productId = product.getId();
        product = productRepository.save(product);
        supplyChain.getProducts().add(product);
        supplyChainRepository.save(supplyChain);
        Optional<Product> optProduct = productRepository.findById(productId);
        if (opt.isEmpty()) {
            return null;
        }
        return optProduct.get();
    }

    /**
     * Removes a product from the specified supply chain and deletes it.
     *
     * @return true if removed; false otherwise
     */
    public boolean deleteProduct(String supplyChainId, String id ) {
        // Verifichiamo se esite dentro al repository un prodotto con l'Id fornito
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            // Verifichiamo se esite dentro al repository una supplychain con l'Id fornito
            Optional<SupplyChain> opt = supplyChainRepository.findById(supplyChainId);
            if (opt.isPresent()) {
                // Siamo sicuri che abbiamo il product e la supplychain di quel product
                SupplyChain supplyChain = opt.get();
                // Rimuoviamo il prodotto dalla supplychain
                supplyChain.getProducts().remove(optionalProduct.get());
                // Salviamo la supplychain
                supplyChainRepository.save(supplyChain);
            }
            //Effettiva delete del prodotto
            productRepository.delete(optionalProduct.get());
            return true;
        }
        return false;
    }

    /** Returns the product list for a supply chain id, or null if chain missing. */
    public List<Product> getProductList(String supplyChainId) {
        Optional<SupplyChain> opt = supplyChainRepository.findById(supplyChainId);
        if (opt.isEmpty()) {
            return null;
        }
        SupplyChain supplyChain = opt.get();
        return supplyChain.getProducts();
    }

    /** Returns all supply chains. */
    public List<SupplyChain> getSupplyChainRepository() {
        return supplyChainRepository.findAll();
    }

    /**
     * Creates and persists a new supply chain with the given name, products and points.
     */
    public SupplyChain createSupplyChain(String supplyChainName, List<Product> products, List<SupplyChainPoint> points) {
        if (supplyChainName == null || supplyChainName.trim().isEmpty()) throw new IllegalArgumentException("Supply chain name cannot be null or empty");
        if (products == null) throw new IllegalArgumentException("Product list cannot be null");

        var supplyChain = buildSupplyChain(supplyChainName, products);
        supplyChain.setPoints(points);
        return supplyChainRepository.save(supplyChain);
    }

    /** Returns supply chains matching the given name pattern. */
    public List<SupplyChain> findSupplyChainsByName(String name) {
        return supplyChainRepository.findByName(name);
    }

    /** Returns products by category. */
    public List<Product> findProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /* ----------------- private helpers ----------------- */

    /** Builds a new {@link SupplyChain} instance. */
    private SupplyChain buildSupplyChain(String name, List<Product> products) {
        return new SupplyChain(name, products);
    }


    /** Persists a new point and adds it to the specified supply chain. */
    public SupplyChainPoint acquirePoint(SupplyChainPoint point, String supplyChainId) {
        Optional<SupplyChain> opt = supplyChainRepository.findById(supplyChainId);
        if (opt.isEmpty()) {
            return null;
        }
        SupplyChain supplyChain = opt.get();
        // we create a new product with a new id
        point.setId(UUID.randomUUID().toString());
        String pointId = point.getId();
        point = supplyChainPointRepository.save(point);
        if (supplyChain.getPoints() == null) {
            supplyChain.setPoints(new java.util.ArrayList<>());
        }
        supplyChain.getPoints().add(point);
        supplyChainRepository.save(supplyChain);
        Optional<SupplyChainPoint> optPoint = supplyChainPointRepository.findById(pointId);
        if (opt.isEmpty()) {
            return null;
        }
        return optPoint.get();
    }
}
