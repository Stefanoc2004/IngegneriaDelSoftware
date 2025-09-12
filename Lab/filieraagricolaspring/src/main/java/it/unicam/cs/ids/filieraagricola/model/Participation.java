package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.sql.Timestamp;
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

    public Participation() {
    }

    public Participation(String id, Event event, User actor, ParticipationRole role, Timestamp registrationDate) {
        this.id = id;
        this.event = event;
        this.actor = actor;
        this.role = role;
        this.registrationDate = registrationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    public ParticipationRole getRole() {
        return role;
    }

    public void setRole(ParticipationRole role) {
        this.role = role;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

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
