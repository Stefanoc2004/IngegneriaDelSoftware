package it.unicam.cs.ids.filieraagricola.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Producer in the agricultural supply chain platform.
 *
 * <p>A Producer is responsible for cultivating and harvesting agricultural products.
 * They can upload information about their products (cultivation methods, certifications, etc.)
 * and sell them in the marketplace. Producers are the starting point of the supply chain.</p>
 *
 * <p>This class extends {@link Actor} and implements specific functionality for
 * managing agricultural production, including cultivation methods, certifications,
 * and the products they produce.</p>
 */
public class Producer extends Actor {

    /**
     * List of cultivation methods used by this producer
     */
    private List<String> cultivationMethods;

    /**
     * List of certifications held by this producer
     */
    private List<String> certifications;

    /**
     * List of product IDs produced by this producer
     */
    private List<String> productIds;

    /**
     * Size of the farm/production area in hectares
     */
    private double farmSizeHectares;

    /**
     * Year when the producer started farming
     */
    private int establishedYear;

    /**
     * Default constructor for frameworks and prototype pattern.
     * Sets the actor type to PRODUCER and initializes collections.
     */
    public Producer() {
        super();
        this.type = ActorType.PRODUCER;
        this.cultivationMethods = new ArrayList<>();
        this.certifications = new ArrayList<>();
        this.productIds = new ArrayList<>();
        this.farmSizeHectares = 0.0;
        this.establishedYear = 0;
    }

    /**
     * Full constructor with validation and normalization.
     *
     * @param id unique identifier (if null or empty, a new UUID will be generated)
     * @param name producer name (must not be null or empty)
     * @param email email address (must not be null or empty)
     * @param phone phone number (may be null)
     * @param address farm address (may be null)
     * @param active whether the producer is active
     * @param cultivationMethods list of cultivation methods (may be null, will be initialized as empty)
     * @param certifications list of certifications (may be null, will be initialized as empty)
     * @param farmSizeHectares size of the farm in hectares (must be >= 0)
     * @param establishedYear year when farming started (must be > 1800 and <= current year)
     * @throws IllegalArgumentException if any validation fails
     */
    public Producer(String id, String name, String email, String phone, String address, boolean active,
                    List<String> cultivationMethods, List<String> certifications,
                    double farmSizeHectares, int establishedYear) {
        super(id, name, email, phone, address, ActorType.PRODUCER, active);

        validateFarmSize(farmSizeHectares);
        validateEstablishedYear(establishedYear);

        this.cultivationMethods = cultivationMethods != null ? new ArrayList<>(cultivationMethods) : new ArrayList<>();
        this.certifications = certifications != null ? new ArrayList<>(certifications) : new ArrayList<>();
        this.productIds = new ArrayList<>();
        this.farmSizeHectares = farmSizeHectares;
        this.establishedYear = establishedYear;
    }

    /**
     * Copy constructor for cloning.
     *
     * @param other the Producer instance to copy
     * @throws IllegalArgumentException if other is null
     */
    public Producer(Producer other) {
        super(other);
        this.cultivationMethods = new ArrayList<>(other.cultivationMethods);
        this.certifications = new ArrayList<>(other.certifications);
        this.productIds = new ArrayList<>(other.productIds);
        this.farmSizeHectares = other.farmSizeHectares;
        this.establishedYear = other.establishedYear;
    }

    /**
     * Creates a deep copy of this Producer instance.
     * This method implements the Prototype pattern.
     *
     * @return a new Producer instance that is a copy of this instance
     */
    @Override
    public Producer clone() {
        return new Producer(this);
    }

    /**
     * Returns the list of cultivation methods used by this producer.
     *
     * @return defensive copy of cultivation methods list
     */
    public List<String> getCultivationMethods() {
        return new ArrayList<>(cultivationMethods);
    }

    /**
     * Sets the cultivation methods for this producer.
     *
     * @param cultivationMethods list of cultivation methods (may be null, will be treated as empty)
     */
    public void setCultivationMethods(List<String> cultivationMethods) {
        this.cultivationMethods = cultivationMethods != null ? new ArrayList<>(cultivationMethods) : new ArrayList<>();
    }

    /**
     * Adds a cultivation method to this producer's methods.
     *
     * @param method cultivation method to add (must not be null or empty)
     * @throws IllegalArgumentException if method is null or empty
     */
    public void addCultivationMethod(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Cultivation method cannot be null or empty");
        }
        String normalizedMethod = method.trim().toLowerCase();
        if (!cultivationMethods.contains(normalizedMethod)) {
            cultivationMethods.add(normalizedMethod);
        }
    }

    /**
     * Removes a cultivation method from this producer's methods.
     *
     * @param method cultivation method to remove
     * @return true if the method was removed, false if it wasn't found
     */
    public boolean removeCultivationMethod(String method) {
        if (method == null) return false;
        return cultivationMethods.remove(method.trim().toLowerCase());
    }

    /**
     * Returns the list of certifications held by this producer.
     *
     * @return defensive copy of certifications list
     */
    public List<String> getCertifications() {
        return new ArrayList<>(certifications);
    }

    /**
     * Sets the certifications for this producer.
     *
     * @param certifications list of certifications (may be null, will be treated as empty)
     */
    public void setCertifications(List<String> certifications) {
        this.certifications = certifications != null ? new ArrayList<>(certifications) : new ArrayList<>();
    }

    /**
     * Adds a certification to this producer's certifications.
     *
     * @param certification certification to add (must not be null or empty)
     * @throws IllegalArgumentException if certification is null or empty
     */
    public void addCertification(String certification) {
        if (certification == null || certification.trim().isEmpty()) {
            throw new IllegalArgumentException("Certification cannot be null or empty");
        }
        String normalizedCert = certification.trim();
        if (!certifications.contains(normalizedCert)) {
            certifications.add(normalizedCert);
        }
    }

    /**
     * Removes a certification from this producer's certifications.
     *
     * @param certification certification to remove
     * @return true if the certification was removed, false if it wasn't found
     */
    public boolean removeCertification(String certification) {
        if (certification == null) return false;
        return certifications.remove(certification.trim());
    }

    /**
     * Returns the list of product IDs produced by this producer.
     *
     * @return defensive copy of product IDs list
     */
    public List<String> getProductIds() {
        return new ArrayList<>(productIds);
    }

    /**
     * Sets the product IDs for this producer.
     *
     * @param productIds list of product IDs (may be null, will be treated as empty)
     */
    public void setProductIds(List<String> productIds) {
        this.productIds = productIds != null ? new ArrayList<>(productIds) : new ArrayList<>();
    }

    /**
     * Adds a product ID to this producer's products.
     *
     * @param productId product ID to add (must not be null or empty)
     * @throws IllegalArgumentException if productId is null or empty
     */
    public void addProductId(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        String normalizedId = productId.trim();
        if (!productIds.contains(normalizedId)) {
            productIds.add(normalizedId);
        }
    }

    /**
     * Removes a product ID from this producer's products.
     *
     * @param productId product ID to remove
     * @return true if the product ID was removed, false if it wasn't found
     */
    public boolean removeProductId(String productId) {
        if (productId == null) return false;
        return productIds.remove(productId.trim());
    }

    /**
     * Returns the farm size in hectares.
     *
     * @return farm size in hectares
     */
    public double getFarmSizeHectares() {
        return farmSizeHectares;
    }

    /**
     * Sets the farm size in hectares.
     *
     * @param farmSizeHectares farm size (must be >= 0)
     * @throws IllegalArgumentException if farmSizeHectares is negative
     */
    public void setFarmSizeHectares(double farmSizeHectares) {
        validateFarmSize(farmSizeHectares);
        this.farmSizeHectares = farmSizeHectares;
    }

    /**
     * Returns the year when the producer started farming.
     *
     * @return established year
     */
    public int getEstablishedYear() {
        return establishedYear;
    }

    /**
     * Sets the year when the producer started farming.
     *
     * @param establishedYear year (must be > 1800 and <= current year)
     * @throws IllegalArgumentException if establishedYear is invalid
     */
    public void setEstablishedYear(int establishedYear) {
        validateEstablishedYear(establishedYear);
        this.establishedYear = establishedYear;
    }

    /**
     * Checks if this producer uses organic cultivation methods.
     *
     * @return true if any cultivation method contains "organic" or "biologico"
     */
    public boolean isOrganicProducer() {
        return cultivationMethods.stream()
                .anyMatch(method -> method.contains("organic") || method.contains("biologico"));
    }

    /**
     * Checks if this producer has any certifications.
     *
     * @return true if the producer has at least one certification
     */
    public boolean hasCertifications() {
        return !certifications.isEmpty();
    }

    /**
     * Checks if this producer has a specific certification.
     *
     * @param certification certification to check for
     * @return true if the producer has the specified certification
     */
    public boolean hasCertification(String certification) {
        if (certification == null) return false;
        return certifications.contains(certification.trim());
    }

    /**
     * Returns the number of products produced by this producer.
     *
     * @return number of products
     */
    public int getProductCount() {
        return productIds.size();
    }

    /**
     * Checks if this producer produces any products.
     *
     * @return true if the producer has at least one product
     */
    public boolean hasProducts() {
        return !productIds.isEmpty();
    }

    // ----------------- validation helpers -----------------

    /**
     * Validates that the farm size is not negative.
     *
     * @param farmSize the farm size to validate
     * @throws IllegalArgumentException if farm size is negative
     */
    private static void validateFarmSize(double farmSize) {
        if (farmSize < 0) {
            throw new IllegalArgumentException("Farm size cannot be negative");
        }
    }

    /**
     * Validates that the established year is reasonable.
     *
     * @param year the year to validate
     * @throws IllegalArgumentException if year is invalid
     */
    private static void validateEstablishedYear(int year) {
        int currentYear = java.time.Year.now().getValue();
        if (year < 1800 || year > currentYear) {
            throw new IllegalArgumentException("Established year must be between 1800 and " + currentYear);
        }
    }

    /**
     * Returns a string representation of this Producer.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Producer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", farmSizeHectares=" + farmSizeHectares +
                ", establishedYear=" + establishedYear +
                ", productCount=" + productIds.size() +
                ", certificationsCount=" + certifications.size() +
                ", active=" + active +
                '}';
    }
}