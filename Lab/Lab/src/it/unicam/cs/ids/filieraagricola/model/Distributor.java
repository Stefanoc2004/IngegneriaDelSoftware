package it.unicam.cs.ids.filieraagricola.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Distributor in the agricultural supply chain platform.
 *
 * <p>A Distributor handles the distribution of typical products and can create packages
 * combining multiple products. They can upload information about products for sale,
 * sell products individually through the marketplace, and create product packages
 * that combine multiple items into a single offer, promoting gastronomic experiences
 * linked to the territory.</p>
 *
 * <p>This class extends {@link Actor} and implements specific functionality for
 * managing product distribution, inventory, delivery methods, and package creation.</p>
 */
public class Distributor extends Actor {

    /**
     * List of single product IDs in the distributor's inventory
     */
    private List<String> singleProductIds;

    /**
     * List of package IDs managed by this distributor
     */
    private List<String> packageIds;

    /**
     * List of delivery methods offered by this distributor
     */
    private List<String> deliveryMethods;

    /**
     * Warehouse capacity in cubic meters
     */
    private double warehouseCapacityM3;

    /**
     * Delivery radius in kilometers
     */
    private double deliveryRadiusKm;

    /**
     * Year when the distribution business was established
     */
    private int establishedYear;

    /**
     * Type of distribution (e.g., "wholesale", "retail", "online", "mixed")
     */
    private String distributionType;

    /**
     * Default constructor for frameworks and prototype pattern.
     * Sets the actor type to DISTRIBUTOR and initializes collections.
     */
    public Distributor() {
        super();
        this.type = ActorType.DISTRIBUTOR;
        this.singleProductIds = new ArrayList<>();
        this.packageIds = new ArrayList<>();
        this.deliveryMethods = new ArrayList<>();
        this.warehouseCapacityM3 = 0.0;
        this.deliveryRadiusKm = 0.0;
        this.establishedYear = 0;
        this.distributionType = "";
    }

    /**
     * Full constructor with validation and normalization.
     *
     * @param id                  unique identifier (if null or empty, a new UUID will be generated)
     * @param name                distributor name (must not be null or empty)
     * @param email               email address (must not be null or empty)
     * @param phone               phone number (may be null)
     * @param address             warehouse address (may be null)
     * @param active              whether the distributor is active
     * @param deliveryMethods     list of delivery methods (may be null, will be initialized as empty)
     * @param warehouseCapacityM3 warehouse capacity in cubic meters (must be >= 0)
     * @param deliveryRadiusKm    delivery radius in kilometers (must be >= 0)
     * @param establishedYear     year when business was established (must be > 1800 and <= current year)
     * @param distributionType    type of distribution business (may be null)
     * @throws IllegalArgumentException if any validation fails
     */
    public Distributor(String id, String name, String email, String phone, String address, boolean active,
                       List<String> deliveryMethods, double warehouseCapacityM3, double deliveryRadiusKm,
                       int establishedYear, String distributionType) {
        super(id, name, email, phone, address, ActorType.DISTRIBUTOR, active);

        validateWarehouseCapacity(warehouseCapacityM3);
        validateDeliveryRadius(deliveryRadiusKm);
        validateEstablishedYear(establishedYear);

        this.singleProductIds = new ArrayList<>();
        this.packageIds = new ArrayList<>();
        this.deliveryMethods = deliveryMethods != null ? new ArrayList<>(deliveryMethods) : new ArrayList<>();
        this.warehouseCapacityM3 = warehouseCapacityM3;
        this.deliveryRadiusKm = deliveryRadiusKm;
        this.establishedYear = establishedYear;
        this.distributionType = distributionType != null ? distributionType.trim().toLowerCase() : "";
    }

    /**
     * Copy constructor for cloning.
     *
     * @param other the Distributor instance to copy
     * @throws IllegalArgumentException if other is null
     */
    public Distributor(Distributor other) {
        super(other);
        this.singleProductIds = new ArrayList<>(other.singleProductIds);
        this.packageIds = new ArrayList<>(other.packageIds);
        this.deliveryMethods = new ArrayList<>(other.deliveryMethods);
        this.warehouseCapacityM3 = other.warehouseCapacityM3;
        this.deliveryRadiusKm = other.deliveryRadiusKm;
        this.establishedYear = other.establishedYear;
        this.distributionType = other.distributionType;
    }

    /**
     * Creates a deep copy of this Distributor instance.
     * This method implements the Prototype pattern.
     *
     * @return a new Distributor instance that is a copy of this instance
     */
    @Override
    public Distributor clone() {
        return new Distributor(this);
    }

    /**
     * Returns the list of single product IDs in inventory.
     *
     * @return defensive copy of single product IDs list
     */
    public List<String> getSingleProductIds() {
        return new ArrayList<>(singleProductIds);
    }

    /**
     * Sets the single product IDs for this distributor.
     *
     * @param singleProductIds list of product IDs (may be null, will be treated as empty)
     */
    public void setSingleProductIds(List<String> singleProductIds) {
        this.singleProductIds = singleProductIds != null ? new ArrayList<>(singleProductIds) : new ArrayList<>();
    }

    /**
     * Adds a single product ID to this distributor's inventory.
     *
     * @param productId product ID to add (must not be null or empty)
     * @throws IllegalArgumentException if productId is null or empty
     */
    public void addSingleProductId(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        String normalizedId = productId.trim();
        if (!singleProductIds.contains(normalizedId)) {
            singleProductIds.add(normalizedId);
        }
    }

    /**
     * Removes a single product ID from this distributor's inventory.
     *
     * @param productId product ID to remove
     * @return true if the product ID was removed, false if it wasn't found
     */
    public boolean removeSingleProductId(String productId) {
        if (productId == null) return false;
        return singleProductIds.remove(productId.trim());
    }

    /**
     * Returns the list of package IDs managed by this distributor.
     *
     * @return defensive copy of package IDs list
     */
    public List<String> getPackageIds() {
        return new ArrayList<>(packageIds);
    }

    /**
     * Sets the package IDs for this distributor.
     *
     * @param packageIds list of package IDs (may be null, will be treated as empty)
     */
    public void setPackageIds(List<String> packageIds) {
        this.packageIds = packageIds != null ? new ArrayList<>(packageIds) : new ArrayList<>();
    }

    /**
     * Adds a package ID to this distributor's packages.
     *
     * @param packageId package ID to add (must not be null or empty)
     * @throws IllegalArgumentException if packageId is null or empty
     */
    public void addPackageId(String packageId) {
        if (packageId == null || packageId.trim().isEmpty()) {
            throw new IllegalArgumentException("Package ID cannot be null or empty");
        }
        String normalizedId = packageId.trim();
        if (!packageIds.contains(normalizedId)) {
            packageIds.add(normalizedId);
        }
    }

    /**
     * Removes a package ID from this distributor's packages.
     *
     * @param packageId package ID to remove
     * @return true if the package ID was removed, false if it wasn't found
     */
    public boolean removePackageId(String packageId) {
        if (packageId == null) return false;
        return packageIds.remove(packageId.trim());
    }

    /**
     * Returns the list of delivery methods offered by this distributor.
     *
     * @return defensive copy of delivery methods list
     */
    public List<String> getDeliveryMethods() {
        return new ArrayList<>(deliveryMethods);
    }

    /**
     * Sets the delivery methods for this distributor.
     *
     * @param deliveryMethods list of delivery methods (may be null, will be treated as empty)
     */
    public void setDeliveryMethods(List<String> deliveryMethods) {
        this.deliveryMethods = deliveryMethods != null ? new ArrayList<>(deliveryMethods) : new ArrayList<>();
    }

    /**
     * Adds a delivery method to this distributor's methods.
     *
     * @param method delivery method to add (must not be null or empty)
     * @throws IllegalArgumentException if method is null or empty
     */
    public void addDeliveryMethod(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery method cannot be null or empty");
        }
        String normalizedMethod = method.trim().toLowerCase();
        if (!deliveryMethods.contains(normalizedMethod)) {
            deliveryMethods.add(normalizedMethod);
        }
    }

    /**
     * Removes a delivery method from this distributor's methods.
     *
     * @param method delivery method to remove
     * @return true if the method was removed, false if it wasn't found
     */
    public boolean removeDeliveryMethod(String method) {
        if (method == null) return false;
        return deliveryMethods.remove(method.trim().toLowerCase());
    }

    /**
     * Returns the warehouse capacity in cubic meters.
     *
     * @return warehouse capacity in m³
     */
    public double getWarehouseCapacityM3() {
        return warehouseCapacityM3;
    }

    /**
     * Sets the warehouse capacity in cubic meters.
     *
     * @param warehouseCapacityM3 warehouse capacity (must be >= 0)
     * @throws IllegalArgumentException if warehouseCapacityM3 is negative
     */
    public void setWarehouseCapacityM3(double warehouseCapacityM3) {
        validateWarehouseCapacity(warehouseCapacityM3);
        this.warehouseCapacityM3 = warehouseCapacityM3;
    }

    /**
     * Returns the delivery radius in kilometers.
     *
     * @return delivery radius in km
     */
    public double getDeliveryRadiusKm() {
        return deliveryRadiusKm;
    }

    /**
     * Sets the delivery radius in kilometers.
     *
     * @param deliveryRadiusKm delivery radius (must be >= 0)
     * @throws IllegalArgumentException if deliveryRadiusKm is negative
     */
    public void setDeliveryRadiusKm(double deliveryRadiusKm) {
        validateDeliveryRadius(deliveryRadiusKm);
        this.deliveryRadiusKm = deliveryRadiusKm;
    }

    /**
     * Returns the year when the distribution business was established.
     *
     * @return established year
     */
    public int getEstablishedYear() {
        return establishedYear;
    }

    /**
     * Sets the year when the distribution business was established.
     *
     * @param establishedYear year (must be > 1800 and <= current year)
     * @throws IllegalArgumentException if establishedYear is invalid
     */
    public void setEstablishedYear(int establishedYear) {
        validateEstablishedYear(establishedYear);
        this.establishedYear = establishedYear;
    }

    /**
     * Returns the type of distribution business.
     *
     * @return distribution type
     */
    public String getDistributionType() {
        return distributionType;
    }

    /**
     * Sets the type of distribution business.
     *
     * @param distributionType distribution type (may be null)
     */
    public void setDistributionType(String distributionType) {
        this.distributionType = distributionType != null ? distributionType.trim().toLowerCase() : "";
    }

    /**
     * Checks if this distributor offers home delivery.
     *
     * @return true if any delivery method contains "home" or "domicilio"
     */
    public boolean offersHomeDelivery() {
        return deliveryMethods.stream()
                .anyMatch(method -> method.contains("home") || method.contains("domicilio"));
    }

    /**
     * Checks if this distributor has products in inventory.
     *
     * @return true if the distributor has at least one single product
     */
    public boolean hasProducts() {
        return !singleProductIds.isEmpty();
    }

    /**
     * Checks if this distributor manages any packages.
     *
     * @return true if the distributor has at least one package
     */
    public boolean hasPackages() {
        return !packageIds.isEmpty();
    }

    /**
     * Returns the total number of products (single products + packages).
     *
     * @return total product count
     */
    public int getTotalProductCount() {
        return singleProductIds.size() + packageIds.size();
    }

    /**
     * Checks if this distributor can deliver to a specific distance.
     *
     * @param distanceKm distance in kilometers
     * @return true if the distance is within the delivery radius
     */
    public boolean canDeliverTo(double distanceKm) {
        return distanceKm >= 0 && distanceKm <= deliveryRadiusKm;
    }

    /**
     * Checks if this distributor is a wholesale distributor.
     *
     * @return true if distribution type contains "wholesale" or "ingrosso"
     */
    public boolean isWholesale() {
        return distributionType.contains("wholesale") || distributionType.contains("ingrosso");
    }

    /**
     * Checks if this distributor is a retail distributor.
     *
     * @return true if distribution type contains "retail" or "dettaglio"
     */
    public boolean isRetail() {
        return distributionType.contains("retail") || distributionType.contains("dettaglio");
    }

    // ----------------- validation helpers -----------------

    /**
     * Validates that the warehouse capacity is not negative.
     *
     * @param capacity the warehouse capacity to validate
     * @throws IllegalArgumentException if capacity is negative
     */
    private static void validateWarehouseCapacity(double capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Warehouse capacity cannot be negative");
        }
    }

    /**
     * Validates that the delivery radius is not negative.
     *
     * @param radius the delivery radius to validate
     * @throws IllegalArgumentException if radius is negative
     */
    private static void validateDeliveryRadius(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Delivery radius cannot be negative");
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
     * Returns a string representation of this Distributor.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Distributor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", distributionType='" + distributionType + '\'' +
                ", warehouseCapacityM3=" + warehouseCapacityM3 +
                ", deliveryRadiusKm=" + deliveryRadiusKm +
                ", establishedYear=" + establishedYear +
                ", productCount=" + singleProductIds.size() +
                ", packageCount=" + packageIds.size() +
                ", active=" + active +
                '}';
    }
}