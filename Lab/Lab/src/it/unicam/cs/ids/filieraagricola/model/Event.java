package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public static Event create(String title, String description,
                               LocalDateTime dateTime, String location,
                               String organizerId) {
        String id = generateId(title, organizerId);
        return new Event(id, title, description, dateTime, location, organizerId);
    }

    public boolean isUpcoming() {
        return false;
    }

    public boolean isPast() {
        return false;
    }

    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }
    }

    private static void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Event title cannot be null or empty");
        }
    }

    private static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Event description cannot be null or empty");
        }
    }

    private static void validateDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Event date and time cannot be null");
        }
        if (dateTime.isBefore(LocalDateTime.of(2025, 1, 1, 0, 0))) {
            throw new IllegalArgumentException("Event date cannot be before year 2025");
        }
    }

    private static void validateLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Event location cannot be null or empty");
        }
    }

    private static void validateOrganizerId(String organizerId) {
        if (organizerId == null || organizerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Organizer ID cannot be null or empty");
        }
    }

    private static String generateId(String title, String organizerId) {
        return (title + "_" + organizerId + "_" + System.currentTimeMillis())
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }


}
