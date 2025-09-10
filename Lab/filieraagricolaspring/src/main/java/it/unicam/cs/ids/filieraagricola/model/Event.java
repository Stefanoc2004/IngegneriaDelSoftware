package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Event {

    @Id
    private String id;
    private String title;
    private String description;
    private Timestamp dateTime;
    private String location;
    private String organizerId;

    public Event(String id, String title, String description, Timestamp dateTime, String location, String organizerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
        this.organizerId = organizerId;
    }

    public Event() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        validateLocation(location);
        this.location = location;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    // New Id to the object
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
