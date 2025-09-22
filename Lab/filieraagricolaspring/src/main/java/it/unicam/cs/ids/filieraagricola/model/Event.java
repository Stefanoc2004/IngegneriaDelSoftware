package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Event entity representing fairs, visits and public activities on the platform.
 *
 * <p>An {@code Event} contains descriptive metadata and scheduling information
 * such as title, description, location, and the organizer identifier. The
 * persistence lifecycle is managed by Spring Data JPA.</p>
 */
@Entity
public class Event {

    @Id
    private String id;
    private String title;
    private String description;
    private Timestamp dateTime;
    private String location;
    private String organizerId;

    /**
     * Full constructor initializing all properties without validation other than
     * what is enforced by setters when used explicitly by callers.
     *
     * @param id           persistent identifier
     * @param title        human-readable event title
     * @param description  short description of the event
     * @param dateTime     scheduled date and time
     * @param location     event location (see {@link #setLocation(String)})
     * @param organizerId  identifier of the organizing actor/user
     */
    public Event(String id, String title, String description, Timestamp dateTime, String location, String organizerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
        this.organizerId = organizerId;
    }

    /**
     * Default constructor required by JPA and frameworks.
     */
    public Event() {

    }


    /**
     * Returns the event identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the event identifier.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the event title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the event title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the event description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the event description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the scheduled date/time.
     */
    public Timestamp getDateTime() {
        return dateTime;
    }

    /**
     * Sets the scheduled date/time.
     */
    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Returns the event location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the event location. Performs basic validation (non-null, non-empty).
     *
     * @param location location string to set
     * @throws IllegalArgumentException if {@code location} is null or empty
     */
    public void setLocation(String location) {
        validateLocation(location);
        this.location = location;
    }

    /**
     * Returns the organizer identifier.
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * Sets the organizer identifier.
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * Assigns a new random UUID string as identifier.
     */
    public void giveNewId() {
        this.id = UUID.randomUUID().toString();
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

}
