package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Animator in the agricultural supply chain platform.
 *
 * <p>An Animator is responsible for organizing events and fairs, inviting producers,
 * transformers, and distributors to participate. They can propose visits to companies,
 * organize presentation events and tasting tours. Animators play a crucial role in
 * promoting the local agricultural supply chain through community engagement and
 * educational activities.</p>
 *
 * <p>This class extends {@link Actor} and implements specific functionality for
 * event organization, participant management, and promotional activities.</p>
 */
public class Animator extends Actor {

    /**
     * List of event IDs organized by this animator
     */
    private List<String> organizedEventIds;

    /**
     * List of company visit IDs organized by this animator
     */
    private List<String> organizedVisitIds;

    /**
     * List of fair IDs where this animator participated as organizer
     */
    private List<String> organizedFairIds;

    /**
     * List of specialization areas for this animator (e.g., "wine tasting", "organic farming")
     */
    private List<String> specializationAreas;

    /**
     * Total number of events organized by this animator
     */
    private int totalEventsOrganized;

    /**
     * Date when the animator started working on the platform
     */
    private LocalDateTime startDate;

    /**
     * Animator's experience level (e.g., "beginner", "intermediate", "expert")
     */
    private String experienceLevel;

    /**
     * Maximum number of events this animator can organize simultaneously
     */
    private int maxSimultaneousEvents;

    /**
     * Average rating received from event participants (0.0 to 5.0)
     */
    private double averageRating;

    /**
     * Total number of participants across all organized events
     */
    private int totalParticipants;

    /**
     * Default constructor for frameworks and prototype pattern.
     * Sets the actor type to ANIMATOR and initializes collections.
     */
    public Animator() {
        super();
        this.type = ActorType.ANIMATOR;
        this.organizedEventIds = new ArrayList<>();
        this.organizedVisitIds = new ArrayList<>();
        this.organizedFairIds = new ArrayList<>();
        this.specializationAreas = new ArrayList<>();
        this.totalEventsOrganized = 0;
        this.startDate = LocalDateTime.now();
        this.experienceLevel = "beginner";
        this.maxSimultaneousEvents = 5;
        this.averageRating = 0.0;
        this.totalParticipants = 0;
    }

    /**
     * Full constructor with validation and normalization.
     *
     * @param id unique identifier (if null or empty, a new UUID will be generated)
     * @param name animator name (must not be null or empty)
     * @param email email address (must not be null or empty)
     * @param phone phone number (may be null)
     * @param address office address (may be null)
     * @param active whether the animator is active
     * @param specializationAreas list of specialization areas (may be null, will be initialized as empty)
     * @param startDate date when animator started (must not be null)
     * @param experienceLevel experience level (may be null, defaults to "beginner")
     * @param maxSimultaneousEvents maximum simultaneous events (must be > 0)
     * @throws IllegalArgumentException if any validation fails
     */
    public Animator(String id, String name, String email, String phone, String address, boolean active,
                    List<String> specializationAreas, LocalDateTime startDate, String experienceLevel,
                    int maxSimultaneousEvents) {
        super(id, name, email, phone, address, ActorType.ANIMATOR, active);

        validateStartDate(startDate);
        validateMaxSimultaneousEvents(maxSimultaneousEvents);

        this.organizedEventIds = new ArrayList<>();
        this.organizedVisitIds = new ArrayList<>();
        this.organizedFairIds = new ArrayList<>();
        this.specializationAreas = specializationAreas != null ? new ArrayList<>(specializationAreas) : new ArrayList<>();
        this.totalEventsOrganized = 0;
        this.startDate = startDate;
        this.experienceLevel = experienceLevel != null ? experienceLevel.trim().toLowerCase() : "beginner";
        this.maxSimultaneousEvents = maxSimultaneousEvents;
        this.averageRating = 0.0;
        this.totalParticipants = 0;
    }

    /**
     * Copy constructor for cloning.
     *
     * @param other the Animator instance to copy
     * @throws IllegalArgumentException if other is null
     */
    public Animator(Animator other) {
        super(other);
        this.organizedEventIds = new ArrayList<>(other.organizedEventIds);
        this.organizedVisitIds = new ArrayList<>(other.organizedVisitIds);
        this.organizedFairIds = new ArrayList<>(other.organizedFairIds);
        this.specializationAreas = new ArrayList<>(other.specializationAreas);
        this.totalEventsOrganized = other.totalEventsOrganized;
        this.startDate = other.startDate;
        this.experienceLevel = other.experienceLevel;
        this.maxSimultaneousEvents = other.maxSimultaneousEvents;
        this.averageRating = other.averageRating;
        this.totalParticipants = other.totalParticipants;
    }

    /**
     * Creates a deep copy of this Animator instance.
     * This method implements the Prototype pattern.
     *
     * @return a new Animator instance that is a copy of this instance
     */
    @Override
    public Animator clone() {
        return new Animator(this);
    }

    /**
     * Returns the list of organized event IDs.
     *
     * @return defensive copy of organized event IDs list
     */
    public List<String> getOrganizedEventIds() {
        return new ArrayList<>(organizedEventIds);
    }

    /**
     * Sets the organized event IDs for this animator.
     *
     * @param organizedEventIds list of organized event IDs (may be null, will be treated as empty)
     */
    public void setOrganizedEventIds(List<String> organizedEventIds) {
        this.organizedEventIds = organizedEventIds != null ? new ArrayList<>(organizedEventIds) : new ArrayList<>();
    }

    /**
     * Adds an organized event ID to this animator's event list.
     *
     * @param eventId event ID to add (must not be null or empty)
     * @throws IllegalArgumentException if eventId is null or empty
     */
    public void addOrganizedEventId(String eventId) {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }
        String normalizedId = eventId.trim();
        if (!organizedEventIds.contains(normalizedId)) {
            organizedEventIds.add(normalizedId);
            totalEventsOrganized++;
        }
    }

    /**
     * Returns the list of organized visit IDs.
     *
     * @return defensive copy of organized visit IDs list
     */
    public List<String> getOrganizedVisitIds() {
        return new ArrayList<>(organizedVisitIds);
    }

    /**
     * Sets the organized visit IDs for this animator.
     *
     * @param organizedVisitIds list of organized visit IDs (may be null, will be treated as empty)
     */
    public void setOrganizedVisitIds(List<String> organizedVisitIds) {
        this.organizedVisitIds = organizedVisitIds != null ? new ArrayList<>(organizedVisitIds) : new ArrayList<>();
    }

    /**
     * Adds an organized visit ID to this animator's visit list.
     *
     * @param visitId visit ID to add (must not be null or empty)
     * @throws IllegalArgumentException if visitId is null or empty
     */
    public void addOrganizedVisitId(String visitId) {
        if (visitId == null || visitId.trim().isEmpty()) {
            throw new IllegalArgumentException("Visit ID cannot be null or empty");
        }
        String normalizedId = visitId.trim();
        if (!organizedVisitIds.contains(normalizedId)) {
            organizedVisitIds.add(normalizedId);
        }
    }

    /**
     * Returns the list of organized fair IDs.
     *
     * @return defensive copy of organized fair IDs list
     */
    public List<String> getOrganizedFairIds() {
        return new ArrayList<>(organizedFairIds);
    }

    /**
     * Sets the organized fair IDs for this animator.
     *
     * @param organizedFairIds list of organized fair IDs (may be null, will be treated as empty)
     */
    public void setOrganizedFairIds(List<String> organizedFairIds) {
        this.organizedFairIds = organizedFairIds != null ? new ArrayList<>(organizedFairIds) : new ArrayList<>();
    }

    /**
     * Adds an organized fair ID to this animator's fair list.
     *
     * @param fairId fair ID to add (must not be null or empty)
     * @throws IllegalArgumentException if fairId is null or empty
     */
    public void addOrganizedFairId(String fairId) {
        if (fairId == null || fairId.trim().isEmpty()) {
            throw new IllegalArgumentException("Fair ID cannot be null or empty");
        }
        String normalizedId = fairId.trim();
        if (!organizedFairIds.contains(normalizedId)) {
            organizedFairIds.add(normalizedId);
        }
    }

    /**
     * Returns the list of specialization areas.
     *
     * @return defensive copy of specialization areas list
     */
    public List<String> getSpecializationAreas() {
        return new ArrayList<>(specializationAreas);
    }

    /**
     * Sets the specialization areas for this animator.
     *
     * @param specializationAreas list of specialization areas (may be null, will be treated as empty)
     */
    public void setSpecializationAreas(List<String> specializationAreas) {
        this.specializationAreas = specializationAreas != null ? new ArrayList<>(specializationAreas) : new ArrayList<>();
    }

    /**
     * Adds a specialization area to this animator's expertise.
     *
     * @param area specialization area to add (must not be null or empty)
     * @throws IllegalArgumentException if area is null or empty
     */
    public void addSpecializationArea(String area) {
        if (area == null || area.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization area cannot be null or empty");
        }
        String normalizedArea = area.trim().toLowerCase();
        if (!specializationAreas.contains(normalizedArea)) {
            specializationAreas.add(normalizedArea);
        }
    }

    /**
     * Returns the total number of events organized.
     *
     * @return total events organized count
     */
    public int getTotalEventsOrganized() {
        return totalEventsOrganized;
    }

    /**
     * Sets the total events organized count.
     *
     * @param totalEventsOrganized total count (must be >= 0)
     * @throws IllegalArgumentException if totalEventsOrganized is negative
     */
    public void setTotalEventsOrganized(int totalEventsOrganized) {
        if (totalEventsOrganized < 0) {
            throw new IllegalArgumentException("Total events organized cannot be negative");
        }
        this.totalEventsOrganized = totalEventsOrganized;
    }

    /**
     * Returns the animator's start date.
     *
     * @return start date
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Sets the animator's start date.
     *
     * @param startDate start date (must not be null)
     * @throws IllegalArgumentException if startDate is null
     */
    public void setStartDate(LocalDateTime startDate) {
        validateStartDate(startDate);
        this.startDate = startDate;
    }

    /**
     * Returns the animator's experience level.
     *
     * @return experience level
     */
    public String getExperienceLevel() {
        return experienceLevel;
    }

    /**
     * Sets the animator's experience level.
     *
     * @param experienceLevel experience level (may be null, defaults to "beginner")
     */
    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel != null ? experienceLevel.trim().toLowerCase() : "beginner";
    }

    /**
     * Returns the maximum simultaneous events capacity.
     *
     * @return maximum simultaneous events
     */
    public int getMaxSimultaneousEvents() {
        return maxSimultaneousEvents;
    }

    /**
     * Sets the maximum simultaneous events capacity.
     *
     * @param maxSimultaneousEvents capacity (must be > 0)
     * @throws IllegalArgumentException if maxSimultaneousEvents is not positive
     */
    public void setMaxSimultaneousEvents(int maxSimultaneousEvents) {
        validateMaxSimultaneousEvents(maxSimultaneousEvents);
        this.maxSimultaneousEvents = maxSimultaneousEvents;
    }

    /**
     * Returns the average rating from participants.
     *
     * @return average rating (0.0 to 5.0)
     */
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * Sets the average rating from participants.
     *
     * @param averageRating rating (must be between 0.0 and 5.0)
     * @throws IllegalArgumentException if rating is out of range
     */
    public void setAverageRating(double averageRating) {
        if (averageRating < 0.0 || averageRating > 5.0) {
            throw new IllegalArgumentException("Average rating must be between 0.0 and 5.0");
        }
        this.averageRating = averageRating;
    }

    /**
     * Returns the total number of participants across all events.
     *
     * @return total participants count
     */
    public int getTotalParticipants() {
        return totalParticipants;
    }

    /**
     * Sets the total participants count.
     *
     * @param totalParticipants total count (must be >= 0)
     * @throws IllegalArgumentException if totalParticipants is negative
     */
    public void setTotalParticipants(int totalParticipants) {
        if (totalParticipants < 0) {
            throw new IllegalArgumentException("Total participants cannot be negative");
        }
        this.totalParticipants = totalParticipants;
    }

    /**
     * Checks if the animator has specialization in a specific area.
     *
     * @param area area to check
     * @return true if the animator has specialization in the area
     */
    public boolean hasSpecializationIn(String area) {
        if (area == null) return false;
        String normalizedArea = area.trim().toLowerCase();
        return specializationAreas.contains(normalizedArea);
    }

    /**
     * Checks if the animator is experienced (intermediate or expert level).
     *
     * @return true if experience level is intermediate or expert
     */
    public boolean isExperienced() {
        return experienceLevel.contains("intermediate") || experienceLevel.contains("expert");
    }

    /**
     * Calculates the average participants per event.
     *
     * @return average participants per event, or 0 if no events organized
     */
    public double getAverageParticipantsPerEvent() {
        if (totalEventsOrganized == 0) return 0.0;
        return (double) totalParticipants / totalEventsOrganized;
    }

    /**
     * Checks if the animator can organize more events simultaneously.
     *
     * @param currentActiveEvents number of currently active events
     * @return true if under maximum capacity
     */
    public boolean canOrganizeMoreEvents(int currentActiveEvents) {
        return currentActiveEvents < maxSimultaneousEvents;
    }

    /**
     * Returns the total number of organized activities (events + visits + fairs).
     *
     * @return total organized activities count
     */
    public int getTotalOrganizedActivities() {
        return organizedEventIds.size() + organizedVisitIds.size() + organizedFairIds.size();
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
     * Validates that the maximum simultaneous events is positive.
     *
     * @param maxEvents the maximum events to validate
     * @throws IllegalArgumentException if maxEvents is not positive
     */
    private static void validateMaxSimultaneousEvents(int maxEvents) {
        if (maxEvents <= 0) {
            throw new IllegalArgumentException("Maximum simultaneous events must be positive");
        }
    }

    /**
     * Returns a string representation of this Animator.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Animator{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", experienceLevel='" + experienceLevel + '\'' +
                ", totalEventsOrganized=" + totalEventsOrganized +
                ", totalParticipants=" + totalParticipants +
                ", averageRating=" + averageRating +
                ", active=" + active +
                '}';
    }
}