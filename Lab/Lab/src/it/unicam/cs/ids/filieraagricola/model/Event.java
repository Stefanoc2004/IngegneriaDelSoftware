package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID; // Added import for UUID

/**
 * Represents an event within the agricultural supply chain platform.
 *
 * <p>Events can include fairs, tastings, markets, conferences or other activities
 * that promote interaction between producers, distributors and consumers.</p>
 *
 * <p>This class implements the Prototype pattern via {@link Prototype#clone()} so
 * callers and services can perform defensive copying when storing or returning
 * Event objects.</p>
 */
public class Event implements Prototype<Event> {

    private String id;
    private String title;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private String organizerId;

    /**
     * Default no-arg constructor for frameworks (e.g. Spring).
     * Generates a unique ID for the event.
     */
    public Event() {
        this.id = UUID.randomUUID().toString(); // Automatically generate ID
    }

    /**
     * Full constructor that validates all provided fields and normalizes strings.
     * If the ID is null or empty, a new one is generated.
     *
     * @param id          unique identifier of the event (if null or empty, a new UUID will be generated)
     * @param title       short descriptive title (must not be null/empty)
     * @param description detailed description (must not be null/empty)
     * @param dateTime    date and time when the event takes place (must not be null and must be >= 2025-01-01)
     * @param location    geographical location (must not be null/empty)
     * @param organizerId identifier of the actor organizing this event (must not be null/empty)
     * @throws IllegalArgumentException when any validation fails
     */
    public Event(String id,
                 String title,
                 String description,
                 LocalDateTime dateTime,
                 String location,
                 String organizerId) {
        this.id = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id.trim(); // Ensure ID is always set and trimmed
        validateTitle(title);
        validateDescription(description);
        validateDateTime(dateTime);
        validateLocation(location);
        validateOrganizerId(organizerId);

        this.title = title.trim();
        this.description = description.trim();
        this.dateTime = dateTime;
        this.location = location.trim();
        this.organizerId = organizerId.trim();
    }

    /**
     * Copy constructor used by {@link #clone()} to implement Prototype pattern.
     *
     * @param other event to copy (must not be null)
     * @throws NullPointerException if other is null
     */
    public Event(Event other) {
        Objects.requireNonNull(other, "Event to copy cannot be null");
        this.id = other.id;
        this.title = other.title;
        this.description = other.description;
        this.dateTime = other.dateTime;
        this.location = other.location;
        this.organizerId = other.organizerId;
    }

    /**
     * Clone this Event (Prototype pattern).
     *
     * @return a new Event instance that is a copy of this instance
     */
    @Override
    public Event clone() {
        return new Event(this);
    }

    /**
     * Returns the event identifier.
     *
     * @return id string, never null after construction
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the event id. Validates input.
     *
     * @param id unique identifier (must not be null/empty)
     * @throws IllegalArgumentException when id invalid
     */
    public void setId(String id) {
        validateId(id);
        this.id = id.trim();
    }

    /**
     * Returns the event title.
     *
     * @return title string
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the event title. Validates and normalizes input.
     *
     * @param title event title (must not be null/empty)
     * @throws IllegalArgumentException when title invalid
     */
    public void setTitle(String title) {
        validateTitle(title);
        this.title = title.trim();
    }

    /**
     * Returns the event description.
     *
     * @return description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the event description. Validates and normalizes input.
     *
     * @param description event description (must not be null/empty)
     * @throws IllegalArgumentException when description invalid
     */
    public void setDescription(String description) {
        validateDescription(description);
        this.description = description.trim();
    }

    /**
     * Returns the date and time of the event.
     *
     * @return LocalDateTime instance
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Sets the event date/time. Validates input.
     *
     * @param dateTime date/time (must not be null and must not be before 2025-01-01)
     * @throws IllegalArgumentException when dateTime invalid
     */
    public void setDateTime(LocalDateTime dateTime) {
        validateDateTime(dateTime);
        this.dateTime = dateTime;
    }

    /**
     * Returns the event location.
     *
     * @return location string
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the event location. Validates and normalizes input.
     *
     * @param location location string (must not be null/empty)
     * @throws IllegalArgumentException when location invalid
     */
    public void setLocation(String location) {
        validateLocation(location);
        this.location = location.trim();
    }

    /**
     * Returns the organizer identifier.
     *
     * @return organizer id string
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * Sets the organizer identifier. Validates input.
     *
     * @param organizerId organizer id (must not be null/empty)
     * @throws IllegalArgumentException when organizerId invalid
     */
    public void setOrganizerId(String organizerId) {
        validateOrganizerId(organizerId);
        this.organizerId = organizerId.trim();
    }

    /**
     * Checks whether the event is scheduled for a future date/time.
     *
     * @return true when event is upcoming
     */
    public boolean isUpcoming() {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Checks whether the event has already taken place.
     *
     * @return true when event is in the past
     */
    public boolean isPast() {
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }

    // ----------------- private validation helpers -----------------

    /**
     * Validates event id parameter.
     *
     * @param id candidate id
     * @throws IllegalArgumentException when id is null or empty
     */
    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Event ID cannot be null or empty");
    }

    /**
     * Validates event title parameter.
     *
     * @param title candidate title
     * @throws IllegalArgumentException when title is null or empty
     */
    private static void validateTitle(String title) {
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Event title cannot be null or empty");
    }

    /**
     * Validates event description parameter.
     *
     * @param description candidate description
     * @throws IllegalArgumentException when description is null or empty
     */
    private static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException("Event description cannot be null or empty");
    }

    /**
     * Validates event date/time parameter.
     * Uses a lower bound (2025-01-01) as sanity check per project constraints.
     *
     * @param dateTime candidate date/time
     * @throws IllegalArgumentException when dateTime is null or before 2025-01-01
     */
    private static void validateDateTime(LocalDateTime dateTime) {
        if (dateTime == null)
            throw new IllegalArgumentException("Event date and time cannot be null");
        if (dateTime.isBefore(LocalDateTime.of(2025, 1, 1, 0, 0)))
            throw new IllegalArgumentException("Event date cannot be before year 2025");
    }

    /**
     * Validates event location parameter.
     *
     * @param location candidate location
     * @throws IllegalArgumentException when location is null or empty
     */
    private static void validateLocation(String location) {
        if (location == null || location.trim().isEmpty())
            throw new IllegalArgumentException("Event location cannot be null or empty");
    }

    /**
     * Validates organizer id parameter.
     *
     * @param organizerId candidate organizer id
     * @throws IllegalArgumentException when organizerId is null or empty
     */
    private static void validateOrganizerId(String organizerId) {
        if (organizerId == null || organizerId.trim().isEmpty())
            throw new IllegalArgumentException("Organizer ID cannot be null or empty");
    }

    // ----------------- equals / hashCode / toString -----------------

    /**
     * Equality is based on event {@code id} when present.
     *
     * @param o other object to compare
     * @return true if equal by id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;
        return id != null && id.equals(event.id);
    }

    /**
     * Hash code derived from {@code id}.
     *
     * @return hash code integer
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    /**
     * Short textual representation for logging and debugging.
     *
     * @return string describing the event
     */
    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", dateTime=" + dateTime +
                ", location='" + location + '\'' +
                '}';
    }
}