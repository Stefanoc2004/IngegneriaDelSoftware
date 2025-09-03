package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a General User in the agricultural supply chain platform.
 *
 * <p>A General User accesses the platform content to learn about the origin
 * and quality of local products. They can browse information, read about
 * producers and their methods, but have limited interaction capabilities
 * compared to other actor types. General Users represent the broader public
 * interested in agricultural transparency and local food systems.</p>
 *
 * <p>This class extends {@link Actor} and implements specific functionality for
 * content browsing, information access, and basic platform interaction.</p>
 */
public class GeneralUser extends Actor {

    /**
     * List of content IDs viewed by this general user
     */
    private List<String> viewedContentIds;

    /**
     * List of producer IDs this user has shown interest in
     */
    private List<String> interestedProducerIds;

    /**
     * List of product categories this user frequently views
     */
    private List<String> frequentlyViewedCategories;

    /**
     * List of search terms this user has used
     */
    private List<String> searchHistory;

    /**
     * Total number of content items viewed by this user
     */
    private int totalContentViewed;

    /**
     * Date when the user first accessed the platform
     */
    private LocalDateTime firstAccessDate;

    /**
     * Date of the user's last activity on the platform
     */
    private LocalDateTime lastActivityDate;

    /**
     * User's engagement level (e.g., "low", "medium", "high")
     */
    private String engagementLevel;

    /**
     * Whether the user has subscribed to newsletters or updates
     */
    private boolean subscribedToUpdates;

    /**
     * Whether the user prefers to view content in a specific language
     */
    private String preferredLanguage;

    /**
     * Number of times the user has shared content on social media
     */
    private int socialSharesCount;

    /**
     * Whether the user has provided feedback on the platform
     */
    private boolean hasProvidedFeedback;

    /**
     * Default constructor for frameworks and prototype pattern.
     * Sets the actor type to GENERAL_USER and initializes collections.
     */
    public GeneralUser() {
        super();
        this.type = ActorType.GENERAL_USER;
        this.viewedContentIds = new ArrayList<>();
        this.interestedProducerIds = new ArrayList<>();
        this.frequentlyViewedCategories = new ArrayList<>();
        this.searchHistory = new ArrayList<>();
        this.totalContentViewed = 0;
        this.firstAccessDate = LocalDateTime.now();
        this.lastActivityDate = LocalDateTime.now();
        this.engagementLevel = "low";
        this.subscribedToUpdates = false;
        this.preferredLanguage = "en";
        this.socialSharesCount = 0;
        this.hasProvidedFeedback = false;
    }

    /**
     * Full constructor with validation and normalization.
     *
     * @param id unique identifier (if null or empty, a new UUID will be generated)
     * @param name user name (must not be null or empty)
     * @param email email address (must not be null or empty)
     * @param phone phone number (may be null)
     * @param address contact address (may be null)
     * @param active whether the user is active
     * @param frequentlyViewedCategories list of frequently viewed categories (may be null, will be initialized as empty)
     * @param firstAccessDate date of first access (must not be null)
     * @param engagementLevel engagement level (may be null, defaults to "low")
     * @param subscribedToUpdates whether subscribed to updates
     * @param preferredLanguage preferred language (may be null, defaults to "en")
     * @throws IllegalArgumentException if any validation fails
     */
    public GeneralUser(String id, String name, String email, String phone, String address, boolean active,
                       List<String> frequentlyViewedCategories, LocalDateTime firstAccessDate, String engagementLevel,
                       boolean subscribedToUpdates, String preferredLanguage) {
        super(id, name, email, phone, address, ActorType.GENERAL_USER, active);

        validateFirstAccessDate(firstAccessDate);

        this.viewedContentIds = new ArrayList<>();
        this.interestedProducerIds = new ArrayList<>();
        this.frequentlyViewedCategories = frequentlyViewedCategories != null ? new ArrayList<>(frequentlyViewedCategories) : new ArrayList<>();
        this.searchHistory = new ArrayList<>();
        this.totalContentViewed = 0;
        this.firstAccessDate = firstAccessDate;
        this.lastActivityDate = firstAccessDate;
        this.engagementLevel = engagementLevel != null ? engagementLevel.trim().toLowerCase() : "low";
        this.subscribedToUpdates = subscribedToUpdates;
        this.preferredLanguage = preferredLanguage != null ? preferredLanguage.trim().toLowerCase() : "en";
        this.socialSharesCount = 0;
        this.hasProvidedFeedback = false;
    }

    /**
     * Copy constructor for cloning.
     *
     * @param other the GeneralUser instance to copy
     * @throws IllegalArgumentException if other is null
     */
    public GeneralUser(GeneralUser other) {
        super(other);
        this.viewedContentIds = new ArrayList<>(other.viewedContentIds);
        this.interestedProducerIds = new ArrayList<>(other.interestedProducerIds);
        this.frequentlyViewedCategories = new ArrayList<>(other.frequentlyViewedCategories);
        this.searchHistory = new ArrayList<>(other.searchHistory);
        this.totalContentViewed = other.totalContentViewed;
        this.firstAccessDate = other.firstAccessDate;
        this.lastActivityDate = other.lastActivityDate;
        this.engagementLevel = other.engagementLevel;
        this.subscribedToUpdates = other.subscribedToUpdates;
        this.preferredLanguage = other.preferredLanguage;
        this.socialSharesCount = other.socialSharesCount;
        this.hasProvidedFeedback = other.hasProvidedFeedback;
    }

    /**
     * Creates a deep copy of this GeneralUser instance.
     * This method implements the Prototype pattern.
     *
     * @return a new GeneralUser instance that is a copy of this instance
     */
    @Override
    public GeneralUser clone() {
        return new GeneralUser(this);
    }

    /**
     * Returns the list of viewed content IDs.
     *
     * @return defensive copy of viewed content IDs list
     */
    public List<String> getViewedContentIds() {
        return new ArrayList<>(viewedContentIds);
    }

    /**
     * Sets the viewed content IDs for this general user.
     *
     * @param viewedContentIds list of viewed content IDs (may be null, will be treated as empty)
     */
    public void setViewedContentIds(List<String> viewedContentIds) {
        this.viewedContentIds = viewedContentIds != null ? new ArrayList<>(viewedContentIds) : new ArrayList<>();
    }

    /**
     * Adds a viewed content ID to this general user's viewing history.
     *
     * @param contentId content ID to add (must not be null or empty)
     * @throws IllegalArgumentException if contentId is null or empty
     */
    public void addViewedContentId(String contentId) {
        if (contentId == null || contentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Content ID cannot be null or empty");
        }
        String normalizedId = contentId.trim();
        viewedContentIds.add(normalizedId); // Allow multiple views of same content
        totalContentViewed++;
        updateLastActivity();
        updateEngagementLevel();
    }

    /**
     * Returns the list of interested producer IDs.
     *
     * @return defensive copy of interested producer IDs list
     */
    public List<String> getInterestedProducerIds() {
        return new ArrayList<>(interestedProducerIds);
    }

    /**
     * Sets the interested producer IDs for this general user.
     *
     * @param interestedProducerIds list of interested producer IDs (may be null, will be treated as empty)
     */
    public void setInterestedProducerIds(List<String> interestedProducerIds) {
        this.interestedProducerIds = interestedProducerIds != null ? new ArrayList<>(interestedProducerIds) : new ArrayList<>();
    }

    /**
     * Adds an interested producer ID to this general user's interests.
     *
     * @param producerId producer ID to add (must not be null or empty)
     * @throws IllegalArgumentException if producerId is null or empty
     */
    public void addInterestedProducerId(String producerId) {
        if (producerId == null || producerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Producer ID cannot be null or empty");
        }
        String normalizedId = producerId.trim();
        if (!interestedProducerIds.contains(normalizedId)) {
            interestedProducerIds.add(normalizedId);
            updateLastActivity();
        }
    }

    /**
     * Returns the list of frequently viewed categories.
     *
     * @return defensive copy of frequently viewed categories list
     */
    public List<String> getFrequentlyViewedCategories() {
        return new ArrayList<>(frequentlyViewedCategories);
    }

    /**
     * Sets the frequently viewed categories for this general user.
     *
     * @param frequentlyViewedCategories list of frequently viewed categories (may be null, will be treated as empty)
     */
    public void setFrequentlyViewedCategories(List<String> frequentlyViewedCategories) {
        this.frequentlyViewedCategories = frequentlyViewedCategories != null ? new ArrayList<>(frequentlyViewedCategories) : new ArrayList<>();
    }

    /**
     * Adds a frequently viewed category to this general user's preferences.
     *
     * @param category category to add (must not be null or empty)
     * @throws IllegalArgumentException if category is null or empty
     */
    public void addFrequentlyViewedCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        String normalizedCategory = category.trim().toLowerCase();
        if (!frequentlyViewedCategories.contains(normalizedCategory)) {
            frequentlyViewedCategories.add(normalizedCategory);
        }
    }

    /**
     * Returns the search history.
     *
     * @return defensive copy of search history list
     */
    public List<String> getSearchHistory() {
        return new ArrayList<>(searchHistory);
    }

    /**
     * Sets the search history for this general user.
     *
     * @param searchHistory list of search terms (may be null, will be treated as empty)
     */
    public void setSearchHistory(List<String> searchHistory) {
        this.searchHistory = searchHistory != null ? new ArrayList<>(searchHistory) : new ArrayList<>();
    }

    /**
     * Adds a search term to this general user's search history.
     *
     * @param searchTerm search term to add (must not be null or empty)
     * @throws IllegalArgumentException if searchTerm is null or empty
     */
    public void addSearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be null or empty");
        }
        String normalizedTerm = searchTerm.trim().toLowerCase();
        searchHistory.add(normalizedTerm); // Allow duplicate searches
        updateLastActivity();
    }

    /**
     * Returns the total number of content items viewed.
     *
     * @return total content viewed count
     */
    public int getTotalContentViewed() {
        return totalContentViewed;
    }

    /**
     * Sets the total content viewed count.
     *
     * @param totalContentViewed total count (must be >= 0)
     * @throws IllegalArgumentException if totalContentViewed is negative
     */
    public void setTotalContentViewed(int totalContentViewed) {
        if (totalContentViewed < 0) {
            throw new IllegalArgumentException("Total content viewed cannot be negative");
        }
        this.totalContentViewed = totalContentViewed;
        updateEngagementLevel();
    }

    /**
     * Returns the first access date.
     *
     * @return first access date
     */
    public LocalDateTime getFirstAccessDate() {
        return firstAccessDate;
    }

    /**
     * Sets the first access date.
     *
     * @param firstAccessDate first access date (must not be null)
     * @throws IllegalArgumentException if firstAccessDate is null
     */
    public void setFirstAccessDate(LocalDateTime firstAccessDate) {
        validateFirstAccessDate(firstAccessDate);
        this.firstAccessDate = firstAccessDate;
    }

    /**
     * Returns the last activity date.
     *
     * @return last activity date
     */
    public LocalDateTime getLastActivityDate() {
        return lastActivityDate;
    }

    /**
     * Sets the last activity date.
     *
     * @param lastActivityDate last activity date (must not be null)
     * @throws IllegalArgumentException if lastActivityDate is null
     */
    public void setLastActivityDate(LocalDateTime lastActivityDate) {
        if (lastActivityDate == null) {
            throw new IllegalArgumentException("Last activity date cannot be null");
        }
        this.lastActivityDate = lastActivityDate;
    }

    /**
     * Returns the engagement level.
     *
     * @return engagement level
     */
    public String getEngagementLevel() {
        return engagementLevel;
    }

    /**
     * Sets the engagement level.
     *
     * @param engagementLevel engagement level (may be null, defaults to "low")
     */
    public void setEngagementLevel(String engagementLevel) {
        this.engagementLevel = engagementLevel != null ? engagementLevel.trim().toLowerCase() : "low";
    }

    /**
     * Returns whether the user is subscribed to updates.
     *
     * @return true if subscribed to updates
     */
    public boolean isSubscribedToUpdates() {
        return subscribedToUpdates;
    }

    /**
     * Sets the subscription to updates preference.
     *
     * @param subscribedToUpdates subscription preference
     */
    public void setSubscribedToUpdates(boolean subscribedToUpdates) {
        this.subscribedToUpdates = subscribedToUpdates;
    }

    /**
     * Returns the preferred language.
     *
     * @return preferred language
     */
    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    /**
     * Sets the preferred language.
     *
     * @param preferredLanguage preferred language (may be null, defaults to "en")
     */
    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage != null ? preferredLanguage.trim().toLowerCase() : "en";
    }

    /**
     * Returns the social shares count.
     *
     * @return social shares count
     */
    public int getSocialSharesCount() {
        return socialSharesCount;
    }

    /**
     * Sets the social shares count.
     *
     * @param socialSharesCount shares count (must be >= 0)
     * @throws IllegalArgumentException if socialSharesCount is negative
     */
    public void setSocialSharesCount(int socialSharesCount) {
        if (socialSharesCount < 0) {
            throw new IllegalArgumentException("Social shares count cannot be negative");
        }
        this.socialSharesCount = socialSharesCount;
    }

    /**
     * Increments the social shares count.
     */
    public void incrementSocialSharesCount() {
        this.socialSharesCount++;
        updateLastActivity();
        updateEngagementLevel();
    }

    /**
     * Returns whether the user has provided feedback.
     *
     * @return true if has provided feedback
     */
    public boolean isHasProvidedFeedback() {
        return hasProvidedFeedback;
    }

    /**
     * Sets whether the user has provided feedback.
     *
     * @param hasProvidedFeedback feedback status
     */
    public void setHasProvidedFeedback(boolean hasProvidedFeedback) {
        this.hasProvidedFeedback = hasProvidedFeedback;
        if (hasProvidedFeedback) {
            updateLastActivity();
            updateEngagementLevel();
        }
    }

    /**
     * Checks if the user frequently views a specific category.
     *
     * @param category category to check
     * @return true if the user frequently views this category
     */
    public boolean frequentlyViewsCategory(String category) {
        if (category == null) return false;
        String normalizedCategory = category.trim().toLowerCase();
        return frequentlyViewedCategories.contains(normalizedCategory);
    }

    /**
     * Checks if the user is highly engaged (high engagement level).
     *
     * @return true if engagement level is high
     */
    public boolean isHighlyEngaged() {
        return engagementLevel.equals("high");
    }

    /**
     * Returns the number of unique content items viewed.
     *
     * @return unique content viewed count
     */
    public int getUniqueContentViewed() {
        return (int) viewedContentIds.stream().distinct().count();
    }

    /**
     * Calculates the days since first access.
     *
     * @return days since first access
     */
    public long getDaysSinceFirstAccess() {
        return java.time.temporal.ChronoUnit.DAYS.between(firstAccessDate.toLocalDate(), LocalDateTime.now().toLocalDate());
    }

    /**
     * Calculates the days since last activity.
     *
     * @return days since last activity
     */
    public long getDaysSinceLastActivity() {
        return java.time.temporal.ChronoUnit.DAYS.between(lastActivityDate.toLocalDate(), LocalDateTime.now().toLocalDate());
    }

    /**
     * Updates the last activity date to current time.
     */
    private void updateLastActivity() {
        this.lastActivityDate = LocalDateTime.now();
    }

    /**
     * Updates the engagement level based on user activity.
     */
    private void updateEngagementLevel() {
        int activityScore = totalContentViewed + socialSharesCount + interestedProducerIds.size();
        if (hasProvidedFeedback) activityScore += 10;
        if (subscribedToUpdates) activityScore += 5;

        if (activityScore >= 50) {
            engagementLevel = "high";
        } else if (activityScore >= 20) {
            engagementLevel = "medium";
        } else {
            engagementLevel = "low";
        }
    }

    // ----------------- validation helpers -----------------

    /**
     * Validates that the first access date is not null.
     *
     * @param firstAccessDate the first access date to validate
     * @throws IllegalArgumentException if firstAccessDate is null
     */
    private static void validateFirstAccessDate(LocalDateTime firstAccessDate) {
        if (firstAccessDate == null) {
            throw new IllegalArgumentException("First access date cannot be null");
        }
    }

    /**
     * Returns a string representation of this GeneralUser.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "GeneralUser{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", engagementLevel='" + engagementLevel + '\'' +
                ", totalContentViewed=" + totalContentViewed +
                ", socialSharesCount=" + socialSharesCount +
                ", subscribedToUpdates=" + subscribedToUpdates +
                ", daysSinceLastActivity=" + getDaysSinceLastActivity() +
                ", active=" + active +
                '}';
    }
}
