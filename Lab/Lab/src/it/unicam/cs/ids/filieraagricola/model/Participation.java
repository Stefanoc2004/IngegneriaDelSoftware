package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;

/**
 * Represents the participation of an actor in a specific event
 * within the agricultural supply chain platform.
 * Each participation links an actor (by ID) with an event (by ID),
 * including the role of the actor during the event.
 *
 * @param id unique identifier for the participation
 * @param eventId identifier of the related event
 * @param actorId identifier of the actor participating
 * @param role role of the actor during the event
 * @param registrationDate date and time when the participation was registered
 */
public record Participation(
        String id,
        String eventId,
        String actorId,
        ParticipationRole role,
        LocalDateTime registrationDate
) {

    /**
     * Compact constructor for validation and normalization.
     */
    public Participation {
        validateId(id);
        validateEventId(eventId);
        validateActorId(actorId);
        validateRole(role);
        validateRegistrationDate(registrationDate);

        eventId = eventId.trim();
        actorId = actorId.trim();
    }

    /**
     * Factory method to create a new Participation with validated parameters.
     *
     * @param eventId the event identifier
     * @param actorId the actor identifier
     * @param role the role of the actor during the event
     * @return a new validated Participation instance
     */
    public static Participation create(String eventId, String actorId, ParticipationRole role) {
        String id = generateId(eventId, actorId);
        LocalDateTime registrationDate = LocalDateTime.now();
        return new Participation(id, eventId, actorId, role, registrationDate);
    }

    /**
     * Checks if the participation is related to a specific event.
     *
     * @param eventId the event ID to check
     * @return true if this participation belongs to the event
     */
    public boolean isForEvent(String eventId) {
        return this.eventId.equals(eventId);
    }

    /**
     * Checks if the participation is related to a specific actor.
     *
     * @param actorId the actor ID to check
     * @return true if this participation belongs to the actor
     */
    public boolean isForActor(String actorId) {
        return this.actorId.equals(actorId);
    }

    /**
     * Validates that the participation ID is not null or empty.
     *
     * @param id the identifier to validate
     * @throws IllegalArgumentException if the ID is null or empty
     */
    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Participation ID cannot be null or empty");
        }
    }

    /**
     * Validates that the event ID is not null or empty.
     *
     * @param eventId the event identifier to validate
     * @throws IllegalArgumentException if the event ID is null or empty
     */
    private static void validateEventId(String eventId) {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }
    }

    /**
     * Validates that the actor ID is not null or empty.
     *
     * @param actorId the actor identifier to validate
     * @throws IllegalArgumentException if the actor ID is null or empty
     */
    private static void validateActorId(String actorId) {
        if (actorId == null || actorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Actor ID cannot be null or empty");
        }
    }

    /**
     * Validates that the participation role is not null.
     *
     * @param role the role to validate
     * @throws IllegalArgumentException if the role is null
     */
    private static void validateRole(ParticipationRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Participation role cannot be null");
        }
    }

    /**
     * Validates that the registration date is not null.
     *
     * @param registrationDate the date to validate
     * @throws IllegalArgumentException if the registration date is null
     */
    private static void validateRegistrationDate(LocalDateTime registrationDate) {
        if (registrationDate == null) {
            throw new IllegalArgumentException("Registration date cannot be null");
        }
    }

    /**
     * Generates a unique participation ID based on event and actor.
     *
     * @param eventId the event ID
     * @param actorId the actor ID
     * @return a normalized unique identifier
     */
    private static String generateId(String eventId, String actorId) {
        return ("part_" + eventId +"_" + actorId + "_" + System.currentTimeMillis())
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }
}
