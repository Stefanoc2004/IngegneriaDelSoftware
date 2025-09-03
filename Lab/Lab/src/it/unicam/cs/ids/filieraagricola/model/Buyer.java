package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Buyer in the agricultural supply chain platform.
 *
 * <p>A Buyer accesses the platform to learn about product origins and can purchase
 * from various supply chain actors. They can also book participation in fairs and
 * events. Buyers are the end consumers who drive demand in the agricultural supply
 * chain and provide feedback on products and experiences.</p>
 *
 * <p>This class extends {@link Actor} and implements specific functionality for
 * purchasing, order management, and event participation.</p>
 */
public class Buyer extends Actor {

    /**
     * List of product IDs purchased by this buyer
     */
    private List<String> purchasedProductIds;

    /**
     * List of order IDs placed by this buyer
     */
    private List<String> orderIds;

    /**
     * List of event IDs where this buyer has participated or booked
     */
    private List<String> participatedEventIds;

    /**
     * List of favorite producer IDs
     */
    private List<String> favoriteProducerIds;

    /**
     * List of product categories this buyer is interested in
     */
    private List<String> preferredCategories;

    /**
     * Total amount spent by this buyer on the platform
     */
    private double totalSpent;

    /**
     * Date when the buyer joined the platform
     */
    private LocalDateTime joinDate;

    /**
     * Buyer's loyalty level (e.g., "bronze", "silver", "gold", "platinum")
     */
    private String loyaltyLevel;

    /**
     * Maximum budget per order for this buyer
     */
    private double maxOrderBudget;

    /**
     * Preferred delivery address for orders
     */
    private String deliveryAddress;

    /**
     * Whether the buyer prefers organic products
     */
    private boolean prefersOrganic;

    /**
     * Whether the buyer wants to receive promotional notifications
     */
    private boolean acceptsPromotions;

    /**
     * Default constructor for frameworks and prototype pattern.
     * Sets the actor type to BUYER and initializes collections.
     */
    public Buyer() {
        super();
        this.type = ActorType.BUYER;
        this.purchasedProductIds = new ArrayList<>();
        this.orderIds = new ArrayList<>();
        this.participatedEventIds = new ArrayList<>();
        this.favoriteProducerIds = new ArrayList<>();
        this.preferredCategories = new ArrayList<>();
        this.totalSpent = 0.0;
        this.joinDate = LocalDateTime.now();
        this.loyaltyLevel = "bronze";
        this.maxOrderBudget = 1000.0;
        this.deliveryAddress = null;
        this.prefersOrganic = false;
        this.acceptsPromotions = true;
    }

    /**
     * Full constructor with validation and normalization.
     *
     * @param id unique identifier (if null or empty, a new UUID will be generated)
     * @param name buyer name (must not be null or empty)
     * @param email email address (must not be null or empty)
     * @param phone phone number (may be null)
     * @param address contact address (may be null)
     * @param active whether the buyer is active
     * @param preferredCategories list of preferred product categories (may be null, will be initialized as empty)
     * @param joinDate date when buyer joined (must not be null)
     * @param loyaltyLevel loyalty level (may be null, defaults to "bronze")
     * @param maxOrderBudget maximum budget per order (must be >= 0)
     * @param deliveryAddress preferred delivery address (may be null)
     * @param prefersOrganic whether buyer prefers organic products
     * @param acceptsPromotions whether buyer accepts promotional notifications
     * @throws IllegalArgumentException if any validation fails
     */
    public Buyer(String id, String name, String email, String phone, String address, boolean active,
                 List<String> preferredCategories, LocalDateTime joinDate, String loyaltyLevel,
                 double maxOrderBudget, String deliveryAddress, boolean prefersOrganic, boolean acceptsPromotions) {
        super(id, name, email, phone, address, ActorType.BUYER, active);

        validateJoinDate(joinDate);
        validateMaxOrderBudget(maxOrderBudget);

        this.purchasedProductIds = new ArrayList<>();
        this.orderIds = new ArrayList<>();
        this.participatedEventIds = new ArrayList<>();
        this.favoriteProducerIds = new ArrayList<>();
        this.preferredCategories = preferredCategories != null ? new ArrayList<>(preferredCategories) : new ArrayList<>();
        this.totalSpent = 0.0;
        this.joinDate = joinDate;
        this.loyaltyLevel = loyaltyLevel != null ? loyaltyLevel.trim().toLowerCase() : "bronze";
        this.maxOrderBudget = maxOrderBudget;
        this.deliveryAddress = deliveryAddress != null ? deliveryAddress.trim() : null;
        this.prefersOrganic = prefersOrganic;
        this.acceptsPromotions = acceptsPromotions;
    }

    /**
     * Copy constructor for cloning.
     *
     * @param other the Buyer instance to copy
     * @throws IllegalArgumentException if other is null
     */
    public Buyer(Buyer other) {
        super(other);
        this.purchasedProductIds = new ArrayList<>(other.purchasedProductIds);
        this.orderIds = new ArrayList<>(other.orderIds);
        this.participatedEventIds = new ArrayList<>(other.participatedEventIds);
        this.favoriteProducerIds = new ArrayList<>(other.favoriteProducerIds);
        this.preferredCategories = new ArrayList<>(other.preferredCategories);
        this.totalSpent = other.totalSpent;
        this.joinDate = other.joinDate;
        this.loyaltyLevel = other.loyaltyLevel;
        this.maxOrderBudget = other.maxOrderBudget;
        this.deliveryAddress = other.deliveryAddress;
        this.prefersOrganic = other.prefersOrganic;
        this.acceptsPromotions = other.acceptsPromotions;
    }

    /**
     * Creates a deep copy of this Buyer instance.
     * This method implements the Prototype pattern.
     *
     * @return a new Buyer instance that is a copy of this instance
     */
    @Override
    public Buyer clone() {
        return new Buyer(this);
    }

    /**
     * Returns the list of purchased product IDs.
     *
     * @return defensive copy of purchased product IDs list
     */
    public List<String> getPurchasedProductIds() {
        return new ArrayList<>(purchasedProductIds);
    }

    /**
     * Sets the purchased product IDs for this buyer.
     *
     * @param purchasedProductIds list of purchased product IDs (may be null, will be treated as empty)
     */
    public void setPurchasedProductIds(List<String> purchasedProductIds) {
        this.purchasedProductIds = purchasedProductIds != null ? new ArrayList<>(purchasedProductIds) : new ArrayList<>();
    }

    /**
     * Adds a purchased product ID to this buyer's purchase history.
     *
     * @param productId product ID to add (must not be null or empty)
     * @param price price paid for the product (must be >= 0)
     * @throws IllegalArgumentException if productId is null or empty, or price is negative
     */
    public void addPurchasedProductId(String productId, double price) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        String normalizedId = productId.trim();
        purchasedProductIds.add(normalizedId); // Allow multiple purchases of same product
        totalSpent += price;
        updateLoyaltyLevel();
    }

    /**
     * Returns the list of order IDs.
     *
     * @return defensive copy of order IDs list
     */
    public List<String> getOrderIds() {
        return new ArrayList<>(orderIds);
    }

    /**
     * Sets the order IDs for this buyer.
     *
     * @param orderIds list of order IDs (may be null, will be treated as empty)
     */
    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds != null ? new ArrayList<>(orderIds) : new ArrayList<>();
    }

    /**
     * Adds an order ID to this buyer's order history.
     *
     * @param orderId order ID to add (must not be null or empty)
     * @throws IllegalArgumentException if orderId is null or empty
     */
    public void addOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        String normalizedId = orderId.trim();
        if (!orderIds.contains(normalizedId)) {
            orderIds.add(normalizedId);
        }
    }

    /**
     * Returns the list of participated event IDs.
     *
     * @return defensive copy of participated event IDs list
     */
    public List<String> getParticipatedEventIds() {
        return new ArrayList<>(participatedEventIds);
    }

    /**
     * Sets the participated event IDs for this buyer.
     *
     * @param participatedEventIds list of participated event IDs (may be null, will be treated as empty)
     */
    public void setParticipatedEventIds(List<String> participatedEventIds) {
        this.participatedEventIds = participatedEventIds != null ? new ArrayList<>(participatedEventIds) : new ArrayList<>();
    }

    /**
     * Adds a participated event ID to this buyer's event history.
     *
     * @param eventId event ID to add (must not be null or empty)
     * @throws IllegalArgumentException if eventId is null or empty
     */
    public void addParticipatedEventId(String eventId) {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }
        String normalizedId = eventId.trim();
        if (!participatedEventIds.contains(normalizedId)) {
            participatedEventIds.add(normalizedId);
        }
    }

    /**
     * Returns the list of favorite producer IDs.
     *
     * @return defensive copy of favorite producer IDs list
     */
    public List<String> getFavoriteProducerIds() {
        return new ArrayList<>(favoriteProducerIds);
    }

    /**
     * Sets the favorite producer IDs for this buyer.
     *
     * @param favoriteProducerIds list of favorite producer IDs (may be null, will be treated as empty)
     */
    public void setFavoriteProducerIds(List<String> favoriteProducerIds) {
        this.favoriteProducerIds = favoriteProducerIds != null ? new ArrayList<>(favoriteProducerIds) : new ArrayList<>();
    }

    /**
     * Adds a favorite producer ID to this buyer's favorites.
     *
     * @param producerId producer ID to add (must not be null or empty)
     * @throws IllegalArgumentException if producerId is null or empty
     */
    public void addFavoriteProducerId(String producerId) {
        if (producerId == null || producerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Producer ID cannot be null or empty");
        }
        String normalizedId = producerId.trim();
        if (!favoriteProducerIds.contains(normalizedId)) {
            favoriteProducerIds.add(normalizedId);
        }
    }

    /**
     * Returns the list of preferred categories.
     *
     * @return defensive copy of preferred categories list
     */
    public List<String> getPreferredCategories() {
        return new ArrayList<>(preferredCategories);
    }

    /**
     * Sets the preferred categories for this buyer.
     *
     * @param preferredCategories list of preferred categories (may be null, will be treated as empty)
     */
    public void setPreferredCategories(List<String> preferredCategories) {
        this.preferredCategories = preferredCategories != null ? new ArrayList<>(preferredCategories) : new ArrayList<>();
    }

    /**
     * Adds a preferred category to this buyer's preferences.
     *
     * @param category category to add (must not be null or empty)
     * @throws IllegalArgumentException if category is null or empty
     */
    public void addPreferredCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        String normalizedCategory = category.trim().toLowerCase();
        if (!preferredCategories.contains(normalizedCategory)) {
            preferredCategories.add(normalizedCategory);
        }
    }

    /**
     * Returns the total amount spent by this buyer.
     *
     * @return total spent amount
     */
    public double getTotalSpent() {
        return totalSpent;
    }

    /**
     * Sets the total spent amount.
     *
     * @param totalSpent total amount (must be >= 0)
     * @throws IllegalArgumentException if totalSpent is negative
     */
    public void setTotalSpent(double totalSpent) {
        if (totalSpent < 0) {
            throw new IllegalArgumentException("Total spent cannot be negative");
        }
        this.totalSpent = totalSpent;
        updateLoyaltyLevel();
    }

    /**
     * Returns the buyer's join date.
     *
     * @return join date
     */
    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    /**
     * Sets the buyer's join date.
     *
     * @param joinDate join date (must not be null)
     * @throws IllegalArgumentException if joinDate is null
     */
    public void setJoinDate(LocalDateTime joinDate) {
        validateJoinDate(joinDate);
        this.joinDate = joinDate;
    }

    /**
     * Returns the buyer's loyalty level.
     *
     * @return loyalty level
     */
    public String getLoyaltyLevel() {
        return loyaltyLevel;
    }

    /**
     * Sets the buyer's loyalty level.
     *
     * @param loyaltyLevel loyalty level (may be null, defaults to "bronze")
     */
    public void setLoyaltyLevel(String loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel != null ? loyaltyLevel.trim().toLowerCase() : "bronze";
    }

    /**
     * Returns the maximum order budget.
     *
     * @return maximum order budget
     */
    public double getMaxOrderBudget() {
        return maxOrderBudget;
    }

    /**
     * Sets the maximum order budget.
     *
     * @param maxOrderBudget budget (must be >= 0)
     * @throws IllegalArgumentException if maxOrderBudget is negative
     */
    public void setMaxOrderBudget(double maxOrderBudget) {
        validateMaxOrderBudget(maxOrderBudget);
        this.maxOrderBudget = maxOrderBudget;
    }

    /**
     * Returns the delivery address.
     *
     * @return delivery address, may be null
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Sets the delivery address.
     *
     * @param deliveryAddress delivery address (may be null)
     */
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress != null ? deliveryAddress.trim() : null;
    }

    /**
     * Returns whether the buyer prefers organic products.
     *
     * @return true if prefers organic products
     */
    public boolean isPrefersOrganic() {
        return prefersOrganic;
    }

    /**
     * Sets the organic preference.
     *
     * @param prefersOrganic organic preference
     */
    public void setPrefersOrganic(boolean prefersOrganic) {
        this.prefersOrganic = prefersOrganic;
    }

    /**
     * Returns whether the buyer accepts promotional notifications.
     *
     * @return true if accepts promotions
     */
    public boolean isAcceptsPromotions() {
        return acceptsPromotions;
    }

    /**
     * Sets the promotional notifications preference.
     *
     * @param acceptsPromotions promotional preference
     */
    public void setAcceptsPromotions(boolean acceptsPromotions) {
        this.acceptsPromotions = acceptsPromotions;
    }

    /**
     * Calculates the average order value.
     *
     * @return average order value, or 0 if no orders
     */
    public double getAverageOrderValue() {
        if (orderIds.isEmpty()) return 0.0;
        return totalSpent / orderIds.size();
    }

    /**
     * Checks if the buyer is a premium customer (gold or platinum level).
     *
     * @return true if loyalty level is gold or platinum
     */
    public boolean isPremiumCustomer() {
        return loyaltyLevel.contains("gold") || loyaltyLevel.contains("platinum");
    }

    /**
     * Checks if the buyer has a preference for a specific category.
     *
     * @param category category to check
     * @return true if the buyer prefers this category
     */
    public boolean prefersCategory(String category) {
        if (category == null) return false;
        String normalizedCategory = category.trim().toLowerCase();
        return preferredCategories.contains(normalizedCategory);
    }

    /**
     * Returns the number of unique products purchased.
     *
     * @return unique products count
     */
    public int getUniqueProductsPurchased() {
        return (int) purchasedProductIds.stream().distinct().count();
    }

    /**
     * Updates the loyalty level based on total spent amount.
     */
    private void updateLoyaltyLevel() {
        if (totalSpent >= 5000) {
            loyaltyLevel = "platinum";
        } else if (totalSpent >= 2000) {
            loyaltyLevel = "gold";
        } else if (totalSpent >= 500) {
            loyaltyLevel = "silver";
        } else {
            loyaltyLevel = "bronze";
        }
    }

    // ----------------- validation helpers -----------------

    /**
     * Validates that the join date is not null.
     *
     * @param joinDate the join date to validate
     * @throws IllegalArgumentException if joinDate is null
     */
    private static void validateJoinDate(LocalDateTime joinDate) {
        if (joinDate == null) {
            throw new IllegalArgumentException("Join date cannot be null");
        }
    }

    /**
     * Validates that the maximum order budget is not negative.
     *
     * @param budget the budget to validate
     * @throws IllegalArgumentException if budget is negative
     */
    private static void validateMaxOrderBudget(double budget) {
        if (budget < 0) {
            throw new IllegalArgumentException("Maximum order budget cannot be negative");
        }
    }

    /**
     * Returns a string representation of this Buyer.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Buyer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", loyaltyLevel='" + loyaltyLevel + '\'' +
                ", totalSpent=" + totalSpent +
                ", ordersCount=" + orderIds.size() +
                ", prefersOrganic=" + prefersOrganic +
                ", active=" + active +
                '}';
    }
}
