package it.unicam.cs.ids.filieraagricola.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Transformer in the agricultural supply chain platform.
 *
 * <p>A Transformer processes raw agricultural products into finished or semi-finished goods.
 * They can upload information about their transformation processes and quality certifications,
 * linking their production phases to local producers. Transformers can also sell their
 * processed products in the marketplace.</p>
 *
 * <p>This class extends {@link Actor} and implements specific functionality for
 * managing product transformation, including processing methods, quality certifications,
 * and supplier relationships.</p>
 */
public class Transformer extends Actor {

    /**
     * List of transformation processes used by this transformer
     */
    private List<String> transformationProcesses;

    /**
     * List of quality certifications held by this transformer
     */
    private List<String> qualityCertifications;

    /**
     * List of supplier (producer) IDs that provide raw materials
     */
    private List<String> supplierIds;

    /**
     * List of processed product IDs created by this transformer
     */
    private List<String> processedProductIds;

    /**
     * Processing capacity per day in kilograms
     */
    private double dailyProcessingCapacityKg;

    /**
     * Year when the transformation facility was established
     */
    private int establishedYear;

    /**
     * Type of facility (e.g., "mill", "winery", "dairy", "bakery")
     */
    private String facilityType;

    /**
     * Default constructor for frameworks and prototype pattern.
     * Sets the actor type to TRANSFORMER and initializes collections.
     */
    public Transformer() {
        super();
        this.type = ActorType.TRANSFORMER;
        this.transformationProcesses = new ArrayList<>();
        this.qualityCertifications = new ArrayList<>();
        this.supplierIds = new ArrayList<>();
        this.processedProductIds = new ArrayList<>();
        this.dailyProcessingCapacityKg = 0.0;
        this.establishedYear = 0;
        this.facilityType = "";
    }

    /**
     * Full constructor with validation and normalization.
     *
     * @param id unique identifier (if null or empty, a new UUID will be generated)
     * @param name transformer name (must not be null or empty)
     * @param email email address (must not be null or empty)
     * @param phone phone number (may be null)
     * @param address facility address (may be null)
     * @param active whether the transformer is active
     * @param transformationProcesses list of transformation processes (may be null)
     * @param qualityCertifications list of quality certifications (may be null)
     * @param dailyProcessingCapacityKg daily processing capacity in kg (must be >= 0)
     * @param establishedYear year when facility was established (must be > 1800 and <= current year)
     * @param facilityType type of transformation facility (may be null)
     * @throws IllegalArgumentException if any validation fails
     */
    public Transformer(String id, String name, String email, String phone, String address, boolean active,
                       List<String> transformationProcesses, List<String> qualityCertifications,
                       double dailyProcessingCapacityKg, int establishedYear, String facilityType) {
        super(id, name, email, phone, address, ActorType.TRANSFORMER, active);

        validateProcessingCapacity(dailyProcessingCapacityKg);
        validateEstablishedYear(establishedYear);

        this.transformationProcesses = transformationProcesses != null ? new ArrayList<>(transformationProcesses) : new ArrayList<>();
        this.qualityCertifications = qualityCertifications != null ? new ArrayList<>(qualityCertifications) : new ArrayList<>();
        this.supplierIds = new ArrayList<>();
        this.processedProductIds = new ArrayList<>();
        this.dailyProcessingCapacityKg = dailyProcessingCapacityKg;
        this.establishedYear = establishedYear;
        this.facilityType = facilityType != null ? facilityType.trim().toLowerCase() : "";
    }

    /**
     * Copy constructor for cloning.
     *
     * @param other the Transformer instance to copy
     * @throws IllegalArgumentException if other is null
     */
    public Transformer(Transformer other) {
        super(other);
        this.transformationProcesses = new ArrayList<>(other.transformationProcesses);
        this.qualityCertifications = new ArrayList<>(other.qualityCertifications);
        this.supplierIds = new ArrayList<>(other.supplierIds);
        this.processedProductIds = new ArrayList<>(other.processedProductIds);
        this.dailyProcessingCapacityKg = other.dailyProcessingCapacityKg;
        this.establishedYear = other.establishedYear;
        this.facilityType = other.facilityType;
    }

    /**
     * Creates a deep copy of this Transformer instance.
     * This method implements the Prototype pattern.
     *
     * @return a new Transformer instance that is a copy of this instance
     */
    @Override
    public Transformer clone() {
        return new Transformer(this);
    }

    /**
     * Returns the list of transformation processes used by this transformer.
     *
     * @return defensive copy of transformation processes list
     */
    public List<String> getTransformationProcesses() {
        return new ArrayList<>(transformationProcesses);
    }

    /**
     * Sets the transformation processes for this transformer.
     *
     * @param transformationProcesses list of transformation processes (may be null)
     */
    public void setTransformationProcesses(List<String> transformationProcesses) {
        this.transformationProcesses = transformationProcesses != null ? new ArrayList<>(transformationProcesses) : new ArrayList<>();
    }

    /**
     * Adds a transformation process to this transformer's processes.
     *
     * @param process transformation process to add (must not be null or empty)
     * @throws IllegalArgumentException if process is null or empty
     */
    public void addTransformationProcess(String process) {
        if (process == null || process.trim().isEmpty()) {
            throw new IllegalArgumentException("Transformation process cannot be null or empty");
        }
        String normalizedProcess = process.trim().toLowerCase();
        if (!transformationProcesses.contains(normalizedProcess)) {
            transformationProcesses.add(normalizedProcess);
        }
    }

    /**
     * Removes a transformation process from this transformer's processes.
     *
     * @param process transformation process to remove
     * @return true if the process was removed, false if it wasn't found
     */
    public boolean removeTransformationProcess(String process) {
        if (process == null) return false;
        return transformationProcesses.remove(process.trim().toLowerCase());
    }

    /**
     * Returns the list of quality certifications held by this transformer.
     *
     * @return defensive copy of quality certifications list
     */
    public List<String> getQualityCertifications() {
        return new ArrayList<>(qualityCertifications);
    }

    /**
     * Sets the quality certifications for this transformer.
     *
     * @param qualityCertifications list of quality certifications (may be null)
     */
    public void setQualityCertifications(List<String> qualityCertifications) {
        this.qualityCertifications = qualityCertifications != null ? new ArrayList<>(qualityCertifications) : new ArrayList<>();
    }

    /**
     * Adds a quality certification to this transformer's certifications.
     *
     * @param certification quality certification to add (must not be null or empty)
     * @throws IllegalArgumentException if certification is null or empty
     */
    public void addQualityCertification(String certification) {
        if (certification == null || certification.trim().isEmpty()) {
            throw new IllegalArgumentException("Quality certification cannot be null or empty");
        }
        String normalizedCert = certification.trim();
        if (!qualityCertifications.contains(normalizedCert)) {
            qualityCertifications.add(normalizedCert);
        }
    }

    /**
     * Removes a quality certification from this transformer's certifications.
     *
     * @param certification quality certification to remove
     * @return true if the certification was removed, false if it wasn't found
     */
    public boolean removeQualityCertification(String certification) {
        if (certification == null) return false;
        return qualityCertifications.remove(certification.trim());
    }

    /**
     * Returns the list of supplier IDs that provide raw materials.
     *
     * @return defensive copy of supplier IDs list
     */
    public List<String> getSupplierIds() {
        return new ArrayList<>(supplierIds);
    }

    /**
     * Sets the supplier IDs for this transformer.
     *
     * @param supplierIds list of supplier IDs (may be null)
     */
    public void setSupplierIds(List<String> supplierIds) {
        this.supplierIds = supplierIds != null ? new ArrayList<>(supplierIds) : new ArrayList<>();
    }

    /**
     * Adds a supplier ID to this transformer's suppliers.
     *
     * @param supplierId supplier ID to add (must not be null or empty)
     * @throws IllegalArgumentException if supplierId is null or empty
     */
    public void addSupplierId(String supplierId) {
        if (supplierId == null || supplierId.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier ID cannot be null or empty");
        }
        String normalizedId = supplierId.trim();
        if (!supplierIds.contains(normalizedId)) {
            supplierIds.add(normalizedId);
        }
    }

    /**
     * Removes a supplier ID from this transformer's suppliers.
     *
     * @param supplierId supplier ID to remove
     * @return true if the supplier ID was removed, false if it wasn't found
     */
    public boolean removeSupplierId(String supplierId) {
        if (supplierId == null) return false;
        return supplierIds.remove(supplierId.trim());
    }

    /**
     * Returns the list of processed product IDs created by this transformer.
     *
     * @return defensive copy of processed product IDs list
     */
    public List<String> getProcessedProductIds() {
        return new ArrayList<>(processedProductIds);
    }

    /**
     * Sets the processed product IDs for this transformer.
     *
     * @param processedProductIds list of processed product IDs (may be null)
     */
    public void setProcessedProductIds(List<String> processedProductIds) {
        this.processedProductIds = processedProductIds != null ? new ArrayList<>(processedProductIds) : new ArrayList<>();
    }

    /**
     * Adds a processed product ID to this transformer's products.
     *
     * @param productId processed product ID to add (must not be null or empty)
     * @throws IllegalArgumentException if productId is null or empty
     */
    public void addProcessedProductId(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Processed product ID cannot be null or empty");
        }
        String normalizedId = productId.trim();
        if (!processedProductIds.contains(normalizedId)) {
            processedProductIds.add(normalizedId);
        }
    }

    /**
     * Removes a processed product ID from this transformer's products.
     *
     * @param productId processed product ID to remove
     * @return true if the product ID was removed, false if it wasn't found
     */
    public boolean removeProcessedProductId(String productId) {
        if (productId == null) return false;
        return processedProductIds.remove(productId.trim());
    }

    /**
     * Returns the daily processing capacity in kilograms.
     *
     * @return daily processing capacity in kg
     */
    public double getDailyProcessingCapacityKg() {
        return dailyProcessingCapacityKg;
    }

    /**
     * Sets the daily processing capacity in kilograms.
     *
     * @param dailyProcessingCapacityKg processing capacity (must be >= 0)
     * @throws IllegalArgumentException if capacity is negative
     */
    public void setDailyProcessingCapacityKg(double dailyProcessingCapacityKg) {
        validateProcessingCapacity(dailyProcessingCapacityKg);
        this.dailyProcessingCapacityKg = dailyProcessingCapacityKg;
    }

    /**
     * Returns the year when the transformation facility was established.
     *
     * @return established year
     */
    public int getEstablishedYear() {
        return establishedYear;
    }

    /**
     * Sets the year when the transformation facility was established.
     *
     * @param establishedYear year (must be > 1800 and <= current year)
     * @throws IllegalArgumentException if establishedYear is invalid
     */
    public void setEstablishedYear(int establishedYear) {
        validateEstablishedYear(establishedYear);
        this.establishedYear = establishedYear;
    }

    /**
     * Returns the type of transformation facility.
     *
     * @return facility type
     */
    public String getFacilityType() {
        return facilityType;
    }

    /**
     * Sets the type of transformation facility.
     *
     * @param facilityType facility type (may be null)
     */
    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType != null ? facilityType.trim().toLowerCase() : "";
    }

    /**
     * Checks if this transformer has quality certifications.
     *
     * @return true if the transformer has at least one quality certification
     */
    public boolean hasQualityCertifications() {
        return !qualityCertifications.isEmpty();
    }

    /**
     * Checks if this transformer has a specific quality certification.
     *
     * @param certification certification to check for
     * @return true if the transformer has the specified certification
     */
    public boolean hasQualityCertification(String certification) {
        if (certification == null) return false;
        return qualityCertifications.contains(certification.trim());
    }

    /**
     * Checks if this transformer works with local suppliers.
     *
     * @return true if the transformer has at least one supplier
     */
    public boolean hasSuppliers() {
        return !supplierIds.isEmpty();
    }

    /**
     * Returns the number of suppliers this transformer works with.
     *
     * @return number of suppliers
     */
    public int getSupplierCount() {
        return supplierIds.size();
    }

    /**
     * Returns the number of processed products created by this transformer.
     *
     * @return number of processed products
     */
    public int getProcessedProductCount() {
        return processedProductIds.size();
    }

    // ----------------- validation helpers -----------------

    /**
     * Validates that the processing capacity is not negative.
     *
     * @param capacity the processing capacity to validate
     * @throws IllegalArgumentException if capacity is negative
     */
    private static void validateProcessingCapacity(double capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Processing capacity cannot be negative");
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
     * Returns a string representation of this Transformer.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Transformer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", facilityType='" + facilityType + '\'' +
                ", dailyProcessingCapacityKg=" + dailyProcessingCapacityKg +
                ", establishedYear=" + establishedYear +
                ", processedProductCount=" + processedProductIds.size() +
                ", supplierCount=" + supplierIds.size() +
                ", active=" + active +
                '}';
    }
}