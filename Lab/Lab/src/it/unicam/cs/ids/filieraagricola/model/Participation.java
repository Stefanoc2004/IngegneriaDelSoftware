package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;

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

    public static Participation create(String eventId, String actorId, ParticipationRole role) {
        String id = generateId(eventId, actorId);
        LocalDateTime registrationDate = LocalDateTime.now();
        return new Participation(id, eventId, actorId, role, registrationDate);
    }

    public boolean isForEvent(String eventId) {
        return this.eventId.equals(eventId);
    }

    public boolean isForActor(String actorId) {
        return this.actorId.equals(actorId);
    }

    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Participation ID cannot be null or empty");
        }
    }

    private static void validateEventId(String eventId) {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }
    }

    private static void validateActorId(String actorId) {
        if (actorId == null || actorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Actor ID cannot be null or empty");
        }
    }

    private static void validateRole(Participation role) {
        if (role == null) {
            throw new IllegalArgumentException("Participation role cannot be null");
        }
    }

    private static void validateRegistrationDate(LocalDateTime registrationDate) {
        if (registrationDate == null) {
            throw new IllegalArgumentException("Registration date cannot be null");
        }
    }

    private static String generateId(String eventId, String actorId) {
        return ("part_" + eventId +"_" + actorId + "_" + System.currentTimeMillis())
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }
}
