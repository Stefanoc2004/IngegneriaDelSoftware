package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an event within the agricultural supply chain platform.
 * Events can include fairs, tastings, markets, conferences, or other activities
 * that promote interaction between producers, distributors, and consumers.
 *
 * @param id unique identifier for the event
 * @param title short descriptive title of the event
 * @param description detailed description of the event
 * @param dateTime date and time when the event takes place
 * @param location geographical location where the event is held
 * @param organizerId identifier of the actor organizing this event
 */
public record Event(
        String id,
        String title,
        String description,
        LocalDateTime dateTime,
        String location,
        String organizerId
) {

    /**
     * Compact constructor for validation and normalization.
     */
    public Event {
        validateId(id);
        validateTitle(title);
        validateDescription(description);
        validateDateTime(dateTime);
        validateLocation(location);
        validateOrganizerId(organizerId);

        title = title.trim();
        description = description.trim();
        location = location.trim();
    }

    /**
     * Factory method to create a new Event with validated parameters.
     *
     * @param title the event title
     * @param description detailed description
     * @param dateTime event date and time
     * @param location event location
     * @param organizerId identifier of the event organizer
     * @return a new validated Event instance
     */
    public static Event create(String title, String description,
                               LocalDateTime dateTime, String location,
                               String organizerId) {
        String id = generateId(title, organizerId);
        return new Event(id, title, description, dateTime, location, organizerId);
    }

    /**
     * Checks if the event is upcoming (scheduled for a future date).
     *
     * @return true if the event is in the future
     */
    public boolean isUpcoming() {
        return dateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Checks if the event has already taken place.
     *
     * @return true if the event date is in the past
     */
    public boolean isPast() {
        return dateTime.isBefore(LocalDateTime.now());
    }

    /**
     * Validates that the event ID is not null or empty.
     *
     * @param id the identifier to validate
     * @throws IllegalArgumentException if the ID is null or empty
     */
    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }
    }

    /**
     * Validates that the event title is not null or empty.
     *
     * @param title the title to validate
     * @throws IllegalArgumentException if the title is null or empty
     */
    private static void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Event title cannot be null or empty");
        }
    }

    /**
     * Validates that the event description is not null or empty.
     *
     * @param description the description to validate
     * @throws IllegalArgumentException if the description is null or empty
     */
    private static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Event description cannot be null or empty");
        }
    }

    /**
     * Validates that the event date and time is not null
     * and is not set too far in the past.
     *
     * @param dateTime the date and time to validate
     * @throws IllegalArgumentException if the date is null or before year 2000
     */
    private static void validateDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Event date and time cannot be null");
        }
        if (dateTime.isBefore(LocalDateTime.of(2025, 1, 1, 0, 0))) {
            throw new IllegalArgumentException("Event date cannot be before year 2025");
        }
    }

    /**
     * Validates that the event location is not null or empty.
     *
     * @param location the location to validate
     * @throws IllegalArgumentException if the location is null or empty
     */
    private static void validateLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Event location cannot be null or empty");
        }
    }

    /**
     * Validates that the organizer ID is not null or empty.
     *
     * @param organizerId the organizer identifier to validate
     * @throws IllegalArgumentException if the organizerId is null or empty
     */
    private static void validateOrganizerId(String organizerId) {
        if (organizerId == null || organizerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Organizer ID cannot be null or empty");
        }
    }

    /**
     * Generates a unique event ID based on its title and organizer ID.
     *
     * @param title the event title
     * @param organizerId the organizer ID
     * @return a normalized unique identifier
     */
    private static String generateId(String title, String organizerId) {
        return (title + "_" + organizerId + "_" + System.currentTimeMillis())
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }


}
