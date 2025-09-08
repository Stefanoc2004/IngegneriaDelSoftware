package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.SupplyChain;
import it.unicam.cs.ids.filieraagricola.model.repositories.ProductRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.SupplyChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
    private SupplyChainRepository supplyChainList;
    @Autowired
    private ProductRepository productList;

    /**
     * Acquires a product into the system.
     *
     * <p>Stores a defensive clone internally and returns a clone to the caller.</p>
     *
     * @param product product to acquire (must not be null)
     */
    public Product acquireProduct(Product product, String supplyChainId) {
        Optional<SupplyChain> opt = supplyChainList.findById(supplyChainId);
        if (opt.isEmpty()) {
            return null;
        }
        SupplyChain supplyChain = opt.get();
        // we create a new product with a new id
        product.setId(UUID.randomUUID().toString());
        String productId = product.getId();
        product = productList.save(product);
        supplyChain.getProducts().add(product);
        supplyChainList.save(supplyChain);
        Optional<Product> optProduct = productList.findById(productId);
        if (opt.isEmpty()) {
            return null;
        }
        return optProduct.get();
    }

    /**
     * Sells (removes) a product from the managed collection.
     *
     * @param product product to sell (must match by equality/id)
     * @return {@code true} if the product was removed; {@code false} otherwise
     * @throws IllegalArgumentException if {@code product} is null or not available
     */
    public boolean deleteProduct(Product product) {
        if (productList.findById(product.getId()).isPresent()) {
            productList.delete(product);
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
        Optional<SupplyChain> opt = supplyChainList.findById(supplyChainId);
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
    public List<SupplyChain> getSupplyChainList() {
        return supplyChainList.findAll();
    }

    /**
     * Creates a new supply chain with the given name and products.
     *
     *
     * @param supplyChainName supply chain name (must not be null/empty)
     * @param products        products to include (must not be null)
     * @throws IllegalArgumentException if inputs are invalid
     */
    public SupplyChain createSupplyChain(String supplyChainName, List<Product> products) {
        if (supplyChainName == null || supplyChainName.trim().isEmpty()) throw new IllegalArgumentException("Supply chain name cannot be null or empty");
        if (products == null) throw new IllegalArgumentException("Product list cannot be null");

        var supplyChain = buildSupplyChain(supplyChainName, products);
        return supplyChainList.save(supplyChain);
    }

    /**
     * Finds supply chains whose name contains the given pattern (case-insensitive).
     *
     * @param name search pattern (may not be null/empty)
     * @return list of matching supply chain clones (empty list if none)
     */
    public List<SupplyChain> findSupplyChainsByName(String name) {
            return supplyChainList.findByName(name);
    }

    /**
     * Finds products by category and returns clones of matching products.
     *
     *
     * @param category category to filter by (must not be null/empty)
     */
    public List<Product> findProductsByCategory(String category) {
        return productList.findByCategory(category);
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
}
