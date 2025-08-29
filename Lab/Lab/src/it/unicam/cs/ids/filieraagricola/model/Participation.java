package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents the participation of an actor in a specific event.
 *
 * <p>Each Participation links an actor (by id) with an event (by id) and
 * includes the role of the actor during the event and the registration timestamp.</p>
 *
 * <p>This class implements the Prototype pattern via {@link Prototype#clone()}
 * so services can defensively copy instances when storing or returning them.</p>
 */
public class Participation implements Prototype {

    private String id;
    private String eventId;
    private String actorId;
    private ParticipationRole role;
    private LocalDateTime registrationDate;

    /**
     * Default no-arg constructor for frameworks. Leaves fields null.
     * registrationDate remains null unless set explicitly.
     */
    public Participation() {
        // for frameworks
    }

    /**
     * Full constructor validating all parameters.
     *
     * @param id               unique participation id (must not be null/empty)
     * @param eventId          related event id (must not be null/empty)
     * @param actorId          actor id (must not be null/empty)
     * @param role             role during the event (must not be null)
     * @param registrationDate timestamp of registration (must not be null)
     * @throws IllegalArgumentException when any validation fails
     */
    public Participation(String id,
                         String eventId,
                         String actorId,
                         ParticipationRole role,
                         LocalDateTime registrationDate) {
        validateId(id);
        validateEventId(eventId);
        validateActorId(actorId);
        validateRole(role);
        validateRegistrationDate(registrationDate);

        this.id = id.trim();
        this.eventId = eventId.trim();
        this.actorId = actorId.trim();
        this.role = role;
        this.registrationDate = registrationDate;
    }

    /**
     * Factory method to create a new Participation with generated id and current registration date.
     *
     * @param eventId event identifier
     * @param actorId actor identifier
     * @param role    role during the event
     * @return newly created Participation instance (validated)
     * @throws IllegalArgumentException when parameters are invalid
     */
    public static Participation create(String eventId, String actorId, ParticipationRole role) {
        String id = generateId(eventId, actorId);
        LocalDateTime registrationDate = LocalDateTime.now();
        return new Participation(id, eventId, actorId, role, registrationDate);
    }

    /**
     * Copy constructor supporting Prototype cloning.
     *
     * @param other participation to copy (must not be null)
     * @throws NullPointerException if other is null
     */
    public Participation(Participation other) {
        Objects.requireNonNull(other, "Participation to copy cannot be null");
        this.id = other.id;
        this.eventId = other.eventId;
        this.actorId = other.actorId;
        this.role = other.role;
        this.registrationDate = other.registrationDate;
    }

    /**
     * Clone this Participation (Prototype pattern).
     *
     * @return a new Participation equivalent to this one
     */
    @Override
    public Participation clone() {
        return new Participation(this);
    }

    /**
     * Returns the participation id.
     *
     * @return id string
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the participation id. Validates input.
     *
     * @param id participation id (must not be null/empty)
     */
    public void setId(String id) {
        validateId(id);
        this.id = id.trim();
    }

    /**
     * Returns the related event id.
     *
     * @return event id string
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Sets the related event id. Validates input.
     *
     * @param eventId event id (must not be null/empty)
     */
    public void setEventId(String eventId) {
        validateEventId(eventId);
        this.eventId = eventId.trim();
    }

    /**
     * Returns the actor id.
     *
     * @return actor id string
     */
    public String getActorId() {
        return actorId;
    }

    /**
     * Sets the actor id. Validates input.
     *
     * @param actorId actor id (must not be null/empty)
     */
    public void setActorId(String actorId) {
        validateActorId(actorId);
        this.actorId = actorId.trim();
    }

    /**
     * Returns the participation role.
     *
     * @return ParticipationRole
     */
    public ParticipationRole getRole() {
        return role;
    }

    /**
     * Sets the participation role. Validates input.
     *
     * @param role participation role (must not be null)
     */
    public void setRole(ParticipationRole role) {
        validateRole(role);
        this.role = role;
    }

    /**
     * Returns the registration timestamp.
     *
     * @return LocalDateTime of registration
     */
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Sets the registration timestamp. Validates input.
     *
     * @param registrationDate registration timestamp (must not be null)
     */
    public void setRegistrationDate(LocalDateTime registrationDate) {
        validateRegistrationDate(registrationDate);
        this.registrationDate = registrationDate;
    }

    /**
     * Returns whether this participation is for the given event id.
     *
     * @param eventId event id to check (may be null)
     * @return true if equal, false otherwise
     */
    public boolean isForEvent(String eventId) {
        return this.eventId != null && this.eventId.equals(eventId);
    }

    /**
     * Returns whether this participation is for the given actor id.
     *
     * @param actorId actor id to check (may be null)
     * @return true if equal, false otherwise
     */
    public boolean isForActor(String actorId) {
        return this.actorId != null && this.actorId.equals(actorId);
    }

    // ----------------- validation helpers -----------------

    /**
     * Validates the participation id parameter.
     *
     * @param id candidate id
     * @throws IllegalArgumentException if id is null or empty
     */
    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Participation ID cannot be null or empty");
    }

    /**
     * Validates the event id parameter.
     *
     * @param eventId candidate event id
     * @throws IllegalArgumentException if eventId is null or empty
     */
    private static void validateEventId(String eventId) {
        if (eventId == null || eventId.trim().isEmpty())
            throw new IllegalArgumentException("Event ID cannot be null or empty");
    }

    /**
     * Validates the actor id parameter.
     *
     * @param actorId candidate actor id
     * @throws IllegalArgumentException if actorId is null or empty
     */
    private static void validateActorId(String actorId) {
        if (actorId == null || actorId.trim().isEmpty())
            throw new IllegalArgumentException("Actor ID cannot be null or empty");
    }

    /**
     * Validates the participation role parameter.
     *
     * @param role candidate role
     * @throws IllegalArgumentException if role is null
     */
    private static void validateRole(ParticipationRole role) {
        if (role == null)
            throw new IllegalArgumentException("Participation role cannot be null");
    }

    /**
     * Validates the registration date parameter.
     *
     * @param registrationDate candidate registration date
     * @throws IllegalArgumentException if registrationDate is null
     */
    private static void validateRegistrationDate(LocalDateTime registrationDate) {
        if (registrationDate == null)
            throw new IllegalArgumentException("Registration date cannot be null");
    }

    /**
     * Generates a unique participation id based on event and actor and current time.
     *
     * @param eventId event id
     * @param actorId actor id
     * @return generated id
     */
    private static String generateId(String eventId, String actorId) {
        return ("part_" + eventId + "_" + actorId + "_" + System.currentTimeMillis())
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }

    // ----------------- equals / hashCode / toString -----------------

    /**
     * Equality is based on participation {@code id} when present.
     *
     * @param o other object
     * @return true if same id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participation that = (Participation) o;
        return id != null && id.equals(that.id);
    }

    /**
     * Hash code based on {@code id}.
     *
     * @return int hash code
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    /**
     * Compact textual representation for logs/debugging.
     *
     * @return string describing participation
     */
    @Override
    public String toString() {
        return "Participation{" +
                "id='" + id + '\'' +
                ", eventId='" + eventId + '\'' +
                ", actorId='" + actorId + '\'' +
                ", role=" + role +
                ", registrationDate=" + registrationDate +
                '}';
    }
}