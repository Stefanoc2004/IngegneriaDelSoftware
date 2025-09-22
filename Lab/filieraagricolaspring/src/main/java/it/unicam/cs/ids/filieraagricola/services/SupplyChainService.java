package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.SupplyChain;
import it.unicam.cs.ids.filieraagricola.model.SupplyChainPoint;
import it.unicam.cs.ids.filieraagricola.model.repositories.ProductRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.SupplayChainPointRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.SupplyChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service responsible for managing supply chains and products.
 *
 * <p>The service stores defensive copies and returns clones to callers, avoiding
 * exposure of internal mutable state. Product cloning is used to enforce the
 * Prototype pattern contract. Public list-returning methods produce lists via
 * {@link java.util.stream.Stream#toList()} to avoid leaking internal collections.</p>
 */
@Service
public class SupplyChainService {

    @Autowired
    private SupplyChainRepository supplyChainRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplayChainPointRepository supplayChainPointRepository;

    /**
     * Acquires a product into the system.
     *
     * <p>Stores a defensive clone internally and returns a clone to the caller.</p>
     *
     * @param product product to acquire (must not be null)
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
     * Sells (removes) a product from the managed collection.
     *
     * @return {@code true} if the product was removed; {@code false} otherwise
     * @throws IllegalArgumentException if {@code product} is null or not available
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

    /**
     * Returns a list of managed products as clones.
     *
     * <p>Each element in the returned list is a defensive clone produced by
     */
    public List<Product> getProductList(String supplyChainId) {
        Optional<SupplyChain> opt = supplyChainRepository.findById(supplyChainId);
        if (opt.isEmpty()) {
            return null;
        }
        SupplyChain supplyChain = opt.get();
        return supplyChain.getProducts();
    }

    /**
     * Returns a list of managed supply chains as clones.
     *
     * <p>Each element in the returned list is a defensive clone produced by
     */
    public List<SupplyChain> getSupplyChainRepository() {
        return supplyChainRepository.findAll();
    }

    /**
     * Creates a new supply chain with the given name and products.
     *
     *
     * @param supplyChainName supply chain name (must not be null/empty)
     * @param products        products to include (must not be null)
     * @throws IllegalArgumentException if inputs are invalid
     */
    public SupplyChain createSupplyChain(String supplyChainName, List<Product> products, List<SupplyChainPoint> points) {
        if (supplyChainName == null || supplyChainName.trim().isEmpty()) throw new IllegalArgumentException("Supply chain name cannot be null or empty");
        if (products == null) throw new IllegalArgumentException("Product list cannot be null");

        var supplyChain = buildSupplyChain(supplyChainName, products);
        supplyChain.setPoints(points);
        return supplyChainRepository.save(supplyChain);
    }

    /**
     * Finds supply chains whose name contains the given pattern (case-insensitive).
     *
     * @param name search pattern (may not be null/empty)
     * @return list of matching supply chain clones (empty list if none)
     */
    public List<SupplyChain> findSupplyChainsByName(String name) {
            return supplyChainRepository.findByName(name);
    }

    /**
     * Finds products by category and returns clones of matching products.
     *
     *
     * @param category category to filter by (must not be null/empty)
     */
    public List<Product> findProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /* ----------------- private helpers ----------------- */

    /**
     * Builds a new SupplyChain instance. Products are cloned to ensure defensive isolation.
     *
     * @param name     supply chain name
     * @return a new SupplyChain instance (internal instance)
     */
    private SupplyChain buildSupplyChain(String name, List<Product> products) {
        return new SupplyChain(name, products);
    }


    /**
     * Acquires a point into the system.
     *
     * <p>Stores a defensive clone internally and returns a clone to the caller.</p>
     *
     */
    public SupplyChainPoint acquirePoint(SupplyChainPoint point, String supplyChainId) {
        Optional<SupplyChain> opt = supplyChainRepository.findById(supplyChainId);
        if (opt.isEmpty()) {
            return null;
        }
        SupplyChain supplyChain = opt.get();
        // we create a new product with a new id
        point.setId(UUID.randomUUID().toString());
        String pointId = point.getId();
        point = supplayChainPointRepository.save(point);
        supplyChain.getPoints().add(point);
        supplyChainRepository.save(supplyChain);
        Optional<SupplyChainPoint> optPoint = supplayChainPointRepository.findById(pointId);
        if (opt.isEmpty()) {
            return null;
        }
        return optPoint.get();
    }
}
