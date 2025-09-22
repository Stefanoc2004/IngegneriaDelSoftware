package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.sql.Timestamp;

/**
 * Association entity linking an {@link Event} and a participating {@link User} with a {@link ParticipationRole}.
 *
 * <p>Represents a user registered to an event (for example as organizer, exhibitor,
 * speaker or visitor) and records the registration timestamp.</p>
 */
@Entity
public class Participation  {
    @Id
    private String id;
    @ManyToOne
    private Event event;
    @ManyToOne
    private User actor;
    private ParticipationRole role;
    private Timestamp registrationDate;

    /** Default constructor required by JPA and frameworks. */
    public Participation() {
    }

    /**
     * Full constructor initializing all fields as provided.
     *
     * @param id                participation identifier
     * @param event             referenced event
     * @param actor             participating user
     * @param role              role assumed during the event
     * @param registrationDate  registration timestamp
     */
    public Participation(String id, Event event, User actor, ParticipationRole role, Timestamp registrationDate) {
        this.id = id;
        this.event = event;
        this.actor = actor;
        this.role = role;
        this.registrationDate = registrationDate;
    }

    /** Returns the participation identifier. */
    public String getId() {
        return id;
    }

    /** Sets the participation identifier. */
    public void setId(String id) {
        this.id = id;
    }

    /** Returns the referenced event. */
    public Event getEvent() {
        return event;
    }

    /** Sets the referenced event. */
    public void setEvent(Event event) {
        this.event = event;
    }

    /** Returns the participating user. */
    public User getActor() {
        return actor;
    }

    /** Sets the participating user. */
    public void setActor(User actor) {
        this.actor = actor;
    }

    /** Returns the role for this participation. */
    public ParticipationRole getRole() {
        return role;
    }

    /** Sets the role for this participation. */
    public void setRole(ParticipationRole role) {
        this.role = role;
    }

    /** Returns the registration timestamp. */
    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    /** Sets the registration timestamp. */
    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Returns a compact textual representation for diagnostics.
     */
    @Override
    public String toString() {
        return "Participation{" +
                "id='" + id + '\'' +
                ", event=" + event +
                ", actor=" + actor +
                ", role=" + role +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
