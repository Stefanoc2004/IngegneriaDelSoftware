package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Participation;
import it.unicam.cs.ids.filieraagricola.model.ParticipationRole;
import it.unicam.cs.ids.filieraagricola.service.exception.NotFoundException;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service responsible for managing event participations.
 *
 * <p>This service handles the creation, retrieval, and management of {@link Participation} objects.
 * It ensures data integrity through validation and provides methods for querying participations.
 * Collections returned by this service are unmodifiable to preserve encapsulation.</p>
 */
public class ParticipationService {

    private final List<Participation> participationList;

    /**
     * Constructs an empty ParticipationService.
     */
    public ParticipationService() {
        this.participationList = new ArrayList<>();
    }

    /**
     * Creates a new Participation with a generated ID and current registration date.
     *
     * @param eventId Event identifier.
     * @param actorId Actor identifier.
     * @param role    Role during the event.
     * @return Newly created Participation instance.
     * @throws ValidationException When parameters are invalid.
     */
    public Participation createParticipation(String eventId, String actorId, ParticipationRole role) {
        validateParticipationData(eventId, actorId, role);
        String id = generateParticipationId(eventId, actorId);
        LocalDateTime registrationDate = LocalDateTime.now();
        Participation newParticipation = new Participation(id, eventId.trim(), actorId.trim(), role, registrationDate);
        participationList.add(newParticipation);
        return newParticipation;
    }

    /**
     * Finds a participation by its unique identifier.
     *
     * @param participationId The ID of the participation to find.
     * @return An Optional containing the found participation, or empty if not found.
     * @throws ValidationException If participationId is null or empty.
     */
    public Optional<Participation> findParticipationById(String participationId) {
        validateId(participationId);
        return participationList.stream()
                .filter(p -> p.getId().equals(participationId))
                .findFirst();
    }

    /**
     * Returns all participations for a specific event.
     *
     * @param eventId The ID of the event.
     * @return An unmodifiable list of participations for the specified event.
     * @throws ValidationException If eventId is null or empty.
     */
    public List<Participation> getParticipationsByEvent(String eventId) {
        validateEventId(eventId);
        return participationList.stream()
                .filter(p -> p.getEventId().equals(eventId))
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns all participations by a specific actor.
     *
     * @param actorId The ID of the actor.
     * @return An unmodifiable list of participations by the specified actor.
     * @throws ValidationException If actorId is null or empty.
     */
    public List<Participation> getParticipationsByActor(String actorId) {
        validateActorId(actorId);
        return participationList.stream()
                .filter(p -> p.getActorId().equals(actorId))
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns all participations with a specific role.
     *
     * @param role The participation role.
     * @return An unmodifiable list of participations with the specified role.
     * @throws ValidationException If role is null.
     */
    public List<Participation> getParticipationsByRole(ParticipationRole role) {
        Objects.requireNonNull(role, "Participation role cannot be null.");
        return participationList.stream()
                .filter(p -> p.getRole() == role)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Removes a participation from the system.
     *
     * @param participationId The ID of the participation to remove.
     * @return True if the participation was removed, false otherwise.
     * @throws ValidationException If participationId is null or empty.
     * @throws NotFoundException If the participation is not found.
     */
    public boolean removeParticipation(String participationId) {
        validateId(participationId);
        Optional<Participation> participationToRemove = findParticipationById(participationId);
        if (participationToRemove.isEmpty()) {
            throw new NotFoundException("Participation with ID " + participationId + " not found.");
        }
        return participationList.remove(participationToRemove.get());
    }

    /**
     * Returns an unmodifiable list of all participations.
     *
     * @return An unmodifiable list containing all managed participations.
     */
    public List<Participation> getAllParticipations() {
        return Collections.unmodifiableList(new ArrayList<>(participationList));
    }

    /**
     * Validates participation data parameters.
     *
     * @param eventId Candidate event ID.
     * @param actorId Candidate actor ID.
     * @param role    Candidate role.
     * @throws ValidationException If any validation fails.
     */
    private void validateParticipationData(String eventId, String actorId, ParticipationRole role) {
        validateEventId(eventId);
        validateActorId(actorId);
        validateRole(role);
    }

    /**
     * Validates the participation ID parameter.
     *
     * @param id Candidate ID.
     * @throws ValidationException If ID is null or empty.
     */
    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("Participation ID cannot be null or empty.");
        }
    }

    /**
     * Validates the event ID parameter.
     *
     * @param eventId Candidate event ID.
     * @throws ValidationException If eventId is null or empty.
     */
    private void validateEventId(String eventId) {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new ValidationException("Event ID cannot be null or empty.");
        }
    }

    /**
     * Validates the actor ID parameter.
     *
     * @param actorId Candidate actor ID.
     * @throws ValidationException If actorId is null or empty.
     */
    private void validateActorId(String actorId) {
        if (actorId == null || actorId.trim().isEmpty()) {
            throw new ValidationException("Actor ID cannot be null or empty.");
        }
    }

    /**
     * Validates the participation role parameter.
     *
     * @param role Candidate role.
     * @throws ValidationException If role is null.
     */
    private void validateRole(ParticipationRole role) {
        if (role == null) {
            throw new ValidationException("Participation role cannot be null.");
        }
    }

    /**
     * Generates a unique participation ID based on event and actor.
     *
     * @param eventId Event ID.
     * @param actorId Actor ID.
     * @return Generated ID.
     */
    private String generateParticipationId(String eventId, String actorId) {
        return "part_" + eventId.replaceAll("\\s+", "_").toLowerCase() +
                "_" + actorId.replaceAll("\\s+", "_").toLowerCase() +
                "_" + UUID.randomUUID().toString().substring(0, 8); // Use a portion of UUID for uniqueness
    }
}
