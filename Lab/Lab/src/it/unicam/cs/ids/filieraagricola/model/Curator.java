package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Curator in the agricultural supply chain platform.
 *
 * <p>A Curator is responsible for verifying content uploaded by producers, transformers,
 * and distributors. They manage the validity of information and approve content before
 * publication. Curators ensure quality control and authenticity of information on the
 * platform, maintaining the integrity of the agricultural supply chain data.</p>
 *
 * <p>This class extends {@link Actor} and implements specific functionality for
 * content moderation, validation, and approval workflows.</p>
 */
public class Curator extends Actor {

    /**
     * List of content IDs that have been approved by this curator
     */
    private List<String> approvedContentIds;

    /**
     * List of content IDs that have been rejected by this curator
     */
    private List<String> rejectedContentIds;

    /**
     * List of content IDs currently pending review by this curator
     */
    private List<String> pendingReviewIds;

    /**
     * List of expertise areas for this curator (e.g., "organic certification", "food safety")
     */
    private List<String> expertiseAreas;

    /**
     * Total number of content items reviewed by this curator
     */
    private int totalReviewedCount;

    /**
     * Date when the curator started working on the platform
     */
    private LocalDateTime startDate;

    /**
     * Curator's qualification level (e.g., "junior", "senior", "expert")
     */
    private String qualificationLevel;

    /**
     * Maximum number of content items this curator can review per day
     */
    private int dailyReviewCapacity;

    /**
     * Default constructor for frameworks and prototype pattern.
     * Sets the actor type to CURATOR and initializes collections.
     */
    public Curator() {
        super();
        this.type = ActorType.CURATOR;
        this.approvedContentIds = new ArrayList<>();
        this.rejectedContentIds = new ArrayList<>();
        this.pendingReviewIds = new ArrayList<>();
        this.expertiseAreas = new ArrayList<>();
        this.totalReviewedCount = 0;
        this.startDate = LocalDateTime.now();
        this.qualificationLevel = "junior";
        this.dailyReviewCapacity = 50;
    }

    /**
     * Full constructor with validation and normalization.
     *
     * @param id unique identifier (if null or empty, a new UUID will be generated)
     * @param name curator name (must not be null or empty)
     * @param email email address (must not be null or empty)
     * @param phone phone number (may be null)
     * @param address office address (may be null)
     * @param active whether the curator is active
     * @param expertiseAreas list of expertise areas (may be null, will be initialized as empty)
     * @param startDate date when curator started (must not be null)
     * @param qualificationLevel qualification level (may be null, defaults to "junior")
     * @param dailyReviewCapacity maximum daily reviews (must be > 0)
     * @throws IllegalArgumentException if any validation fails
     */
    public Curator(String id, String name, String email, String phone, String address, boolean active,
                   List<String> expertiseAreas, LocalDateTime startDate, String qualificationLevel,
                   int dailyReviewCapacity) {
        super(id, name, email, phone, address, ActorType.CURATOR, active);

        validateStartDate(startDate);
        validateDailyReviewCapacity(dailyReviewCapacity);

        this.approvedContentIds = new ArrayList<>();
        this.rejectedContentIds = new ArrayList<>();
        this.pendingReviewIds = new ArrayList<>();
        this.expertiseAreas = expertiseAreas != null ? new ArrayList<>(expertiseAreas) : new ArrayList<>();
        this.totalReviewedCount = 0;
        this.startDate = startDate;
        this.qualificationLevel = qualificationLevel != null ? qualificationLevel.trim().toLowerCase() : "junior";
        this.dailyReviewCapacity = dailyReviewCapacity;
    }

    /**
     * Copy constructor for cloning.
     *
     * @param other the Curator instance to copy
     * @throws IllegalArgumentException if other is null
     */
    public Curator(Curator other) {
        super(other);
        this.approvedContentIds = new ArrayList<>(other.approvedContentIds);
        this.rejectedContentIds = new ArrayList<>(other.rejectedContentIds);
        this.pendingReviewIds = new ArrayList<>(other.pendingReviewIds);
        this.expertiseAreas = new ArrayList<>(other.expertiseAreas);
        this.totalReviewedCount = other.totalReviewedCount;
        this.startDate = other.startDate;
        this.qualificationLevel = other.qualificationLevel;
        this.dailyReviewCapacity = other.dailyReviewCapacity;
    }

    /**
     * Creates a deep copy of this Curator instance.
     * This method implements the Prototype pattern.
     *
     * @return a new Curator instance that is a copy of this instance
     */
    @Override
    public Curator clone() {
        return new Curator(this);
    }

    /**
     * Returns the list of approved content IDs.
     *
     * @return defensive copy of approved content IDs list
     */
    public List<String> getApprovedContentIds() {
        return new ArrayList<>(approvedContentIds);
    }

    /**
     * Sets the approved content IDs for this curator.
     *
     * @param approvedContentIds list of approved content IDs (may be null, will be treated as empty)
     */
    public void setApprovedContentIds(List<String> approvedContentIds) {
        this.approvedContentIds = approvedContentIds != null ? new ArrayList<>(approvedContentIds) : new ArrayList<>();
    }

    /**
     * Adds an approved content ID to this curator's approved list.
     *
     * @param contentId content ID to add (must not be null or empty)
     * @throws IllegalArgumentException if contentId is null or empty
     */
    public void addApprovedContentId(String contentId) {
        if (contentId == null || contentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Content ID cannot be null or empty");
        }
        String normalizedId = contentId.trim();
        if (!approvedContentIds.contains(normalizedId)) {
            approvedContentIds.add(normalizedId);
            totalReviewedCount++;
            // Remove from pending if it was there
            pendingReviewIds.remove(normalizedId);
        }
    }

    /**
     * Returns the list of rejected content IDs.
     *
     * @return defensive copy of rejected content IDs list
     */
    public List<String> getRejectedContentIds() {
        return new ArrayList<>(rejectedContentIds);
    }

    /**
     * Sets the rejected content IDs for this curator.
     *
     * @param rejectedContentIds list of rejected content IDs (may be null, will be treated as empty)
     */
    public void setRejectedContentIds(List<String> rejectedContentIds) {
        this.rejectedContentIds = rejectedContentIds != null ? new ArrayList<>(rejectedContentIds) : new ArrayList<>();
    }

    /**
     * Adds a rejected content ID to this curator's rejected list.
     *
     * @param contentId content ID to add (must not be null or empty)
     * @param reason reason for rejection (must not be null or empty)
     * @throws IllegalArgumentException if contentId or reason is null or empty
     */
    public void addRejectedContentId(String contentId, String reason) {
        if (contentId == null || contentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Content ID cannot be null or empty");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Rejection reason cannot be null or empty");
        }
        String normalizedId = contentId.trim();
        if (!rejectedContentIds.contains(normalizedId)) {
            rejectedContentIds.add(normalizedId);
            totalReviewedCount++;
            // Remove from pending if it was there
            pendingReviewIds.remove(normalizedId);
        }
    }

    /**
     * Returns the list of pending review content IDs.
     *
     * @return defensive copy of pending review IDs list
     */
    public List<String> getPendingReviewIds() {
        return new ArrayList<>(pendingReviewIds);
    }

    /**
     * Sets the pending review IDs for this curator.
     *
     * @param pendingReviewIds list of pending review IDs (may be null, will be treated as empty)
     */
    public void setPendingReviewIds(List<String> pendingReviewIds) {
        this.pendingReviewIds = pendingReviewIds != null ? new ArrayList<>(pendingReviewIds) : new ArrayList<>();
    }

    /**
     * Adds a content ID to this curator's pending review list.
     *
     * @param contentId content ID to add for review (must not be null or empty)
     * @throws IllegalArgumentException if contentId is null or empty
     */
    public void addPendingReviewId(String contentId) {
        if (contentId == null || contentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Content ID cannot be null or empty");
        }
        String normalizedId = contentId.trim();
        if (!pendingReviewIds.contains(normalizedId) &&
                !approvedContentIds.contains(normalizedId) &&
                !rejectedContentIds.contains(normalizedId)) {
            pendingReviewIds.add(normalizedId);
        }
    }

    /**
     * Returns the list of expertise areas.
     *
     * @return defensive copy of expertise areas list
     */
    public List<String> getExpertiseAreas() {
        return new ArrayList<>(expertiseAreas);
    }

    /**
     * Sets the expertise areas for this curator.
     *
     * @param expertiseAreas list of expertise areas (may be null, will be treated as empty)
     */
    public void setExpertiseAreas(List<String> expertiseAreas) {
        this.expertiseAreas = expertiseAreas != null ? new ArrayList<>(expertiseAreas) : new ArrayList<>();
    }

    /**
     * Adds an expertise area to this curator's expertise.
     *
     * @param area expertise area to add (must not be null or empty)
     * @throws IllegalArgumentException if area is null or empty
     */
    public void addExpertiseArea(String area) {
        if (area == null || area.trim().isEmpty()) {
            throw new IllegalArgumentException("Expertise area cannot be null or empty");
        }
        String normalizedArea = area.trim().toLowerCase();
        if (!expertiseAreas.contains(normalizedArea)) {
            expertiseAreas.add(normalizedArea);
        }
    }

    /**
     * Returns the total number of content items reviewed.
     *
     * @return total reviewed count
     */
    public int getTotalReviewedCount() {
        return totalReviewedCount;
    }

    /**
     * Sets the total reviewed count.
     *
     * @param totalReviewedCount total count (must be >= 0)
     * @throws IllegalArgumentException if totalReviewedCount is negative
     */
    public void setTotalReviewedCount(int totalReviewedCount) {
        if (totalReviewedCount < 0) {
            throw new IllegalArgumentException("Total reviewed count cannot be negative");
        }
        this.totalReviewedCount = totalReviewedCount;
    }

    /**
     * Returns the curator's start date.
     *
     * @return start date
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Sets the curator's start date.
     *
     * @param startDate start date (must not be null)
     * @throws IllegalArgumentException if startDate is null
     */
    public void setStartDate(LocalDateTime startDate) {
        validateStartDate(startDate);
        this.startDate = startDate;
    }

    /**
     * Returns the curator's qualification level.
     *
     * @return qualification level
     */
    public String getQualificationLevel() {
        return qualificationLevel;
    }

    /**
     * Sets the curator's qualification level.
     *
     * @param qualificationLevel qualification level (may be null, defaults to "junior")
     */
    public void setQualificationLevel(String qualificationLevel) {
        this.qualificationLevel = qualificationLevel != null ? qualificationLevel.trim().toLowerCase() : "junior";
    }

    /**
     * Returns the daily review capacity.
     *
     * @return daily review capacity
     */
    public int getDailyReviewCapacity() {
        return dailyReviewCapacity;
    }

    /**
     * Sets the daily review capacity.
     *
     * @param dailyReviewCapacity capacity (must be > 0)
     * @throws IllegalArgumentException if dailyReviewCapacity is not positive
     */
    public void setDailyReviewCapacity(int dailyReviewCapacity) {
        validateDailyReviewCapacity(dailyReviewCapacity);
        this.dailyReviewCapacity = dailyReviewCapacity;
    }

    /**
     * Calculates the approval rate of this curator.
     *
     * @return approval rate as a percentage (0-100), or 0 if no reviews
     */
    public double getApprovalRate() {
        int totalDecisions = approvedContentIds.size() + rejectedContentIds.size();
        if (totalDecisions == 0) return 0.0;
        return (approvedContentIds.size() * 100.0) / totalDecisions;
    }

    /**
     * Checks if the curator has expertise in a specific area.
     *
     * @param area area to check
     * @return true if the curator has expertise in the area
     */
    public boolean hasExpertiseIn(String area) {
        if (area == null) return false;
        String normalizedArea = area.trim().toLowerCase();
        return expertiseAreas.contains(normalizedArea);
    }

    /**
     * Checks if the curator is experienced (senior or expert level).
     *
     * @return true if qualification level is senior or expert
     */
    public boolean isExperienced() {
        return qualificationLevel.contains("senior") || qualificationLevel.contains("expert");
    }

    /**
     * Returns the number of pending reviews.
     *
     * @return pending review count
     */
    public int getPendingReviewCount() {
        return pendingReviewIds.size();
    }

    /**
     * Checks if the curator can take more reviews today.
     *
     * @param todayReviewCount number of reviews done today
     * @return true if under daily capacity
     */
    public boolean canReviewMore(int todayReviewCount) {
        return todayReviewCount < dailyReviewCapacity;
    }

    // ----------------- validation helpers -----------------

    /**
     * Validates that the start date is not null.
     *
     * @param startDate the start date to validate
     * @throws IllegalArgumentException if startDate is null
     */
    private static void validateStartDate(LocalDateTime startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
    }

    /**
     * Validates that the daily review capacity is positive.
     *
     * @param capacity the capacity to validate
     * @throws IllegalArgumentException if capacity is not positive
     */
    private static void validateDailyReviewCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Daily review capacity must be positive");
        }
    }

    /**
     * Returns a string representation of this Curator.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Curator{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", qualificationLevel='" + qualificationLevel + '\'' +
                ", totalReviewedCount=" + totalReviewedCount +
                ", approvedCount=" + approvedContentIds.size() +
                ", rejectedCount=" + rejectedContentIds.size() +
                ", pendingCount=" + pendingReviewIds.size() +
                ", active=" + active +
                '}';
    }
}