package it.unicam.cs.ids.filieraagricola.model;

import java.time.LocalDateTime;

/**
 * Represents the participation of an actor in a specific event.
 *
 * <p>Each Participation links an actor (by id) with an event (by id) and
 * includes the role of the actor during the event and the registration timestamp.
 * This class is a pure data model and does not contain business logic for validation or creation.
 * Business logic, such as participation creation and validation, is handled by {@link it.unicam.cs.ids.filieraagricola.service.ParticipationService}.</p>
 */
public class Participation {

    private final String id;
    private final String eventId;
    private final String actorId;
    private final ParticipationRole role;
    private final LocalDateTime registrationDate;

    /**
     * Constructs a new Participation instance.
     * This constructor is primarily for internal use by services when creating new participations.
     *
     * @param id               Unique participation ID.
     * @param eventId          Related event ID.
     * @param actorId          Actor ID.
     * @param role             Role during the event.
     * @param registrationDate Timestamp of registration.
     */
    public Participation(String id, String eventId, String actorId, ParticipationRole role, LocalDateTime registrationDate) {
        this.id = id;
        this.eventId = eventId;
        this.actorId = actorId;
        this.role = role;
        this.registrationDate = registrationDate;
    }

    /**
     * Returns the participation ID.
     *
     * @return ID string.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the related event ID.
     *
     * @return Event ID string.
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Returns the actor ID.
     *
     * @return Actor ID string.
     */
    public String getActorId() {
        return actorId;
    }

    /**
     * Returns the participation role.
     *
     * @return ParticipationRole.
     */
    public ParticipationRole getRole() {
        return role;
    }

    /**
     * Returns the registration timestamp.
     *
     * @return LocalDateTime of registration.
     */
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Indicates whether some other object is "equal to" this Participation.
     * Two Participation objects are considered equal if they have the same ID.
     *
     * @param o The reference object with which to compare.
     * @return True if this object is equal to the o argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participation that = (Participation) o;
        return id != null && id.equals(that.id);
    }

    /**
     * Returns a hash code value for this Participation.
     * The hash code is based on the participation ID.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    /**
     * Compact textual representation for logs/debugging.
     *
     * @return String describing participation.
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
