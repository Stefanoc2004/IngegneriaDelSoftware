package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents an event within the agricultural supply chain platform.
 *
 * <p>Events can include fairs, tastings, markets, conferences or other activities
 * that promote interaction between producers, distributors and consumers.
 * This class is a pure data model and does not contain business logic for validation or state checks.
 * Business logic, such as event validation and scheduling, is handled by {@link it.unicam.cs.ids.filieraagricola.service.EventService}.</p>
 */
public class Event {

    private final String id;
    private final String title;
    private final String description;
    private final LocalDateTime dateTime;
    private final String location;
    private final String organizerId;

    /**
     * Constructs a new Event instance with a generated ID.
     * This constructor is primarily for internal use by services when creating new events.
     *
     * @param title       Short descriptive title.
     * @param description Detailed description.
     * @param dateTime    Date and time when the event takes place.
     * @param location    Geographical location.
     * @param organizerId Identifier of the actor organizing this event.
     */
    public Event(String title, String description, LocalDateTime dateTime, String location, String organizerId) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
        this.organizerId = organizerId;
    }

    /**
     * Constructs a new Event instance with a specified ID.
     * This constructor is typically used when reconstructing an Event object from persistence
     * or when a specific ID is required.
     *
     * @param id          Unique identifier of the event.
     * @param title       Short descriptive title.
     * @param description Detailed description.
     * @param dateTime    Date and time when the event takes place.
     * @param location    Geographical location.
     * @param organizerId Identifier of the actor organizing this event.
     */
    public Event(String id, String title, String description, LocalDateTime dateTime, String location, String organizerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
        this.organizerId = organizerId;
    }

    /**
     * Returns the event identifier.
     *
     * @return ID string, never null after construction.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the event title.
     *
     * @return Title string.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the event description.
     *
     * @return Description string.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the date and time of the event.
     *
     * @return LocalDateTime instance.
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns the event location.
     *
     * @return Location string.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the organizer identifier.
     *
     * @return Organizer ID string.
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * Equality is based on event {@code id} when present.
     *
     * @param o Other object to compare.
     * @return True if equal by ID.
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
     * @return Hash code integer.
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    /**
     * Short textual representation for logging and debugging.
     *
     * @return String describing the event.
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
