package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Event;
import it.unicam.cs.ids.filieraagricola.model.Participation;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service responsible for managing events and participations.
 *
 * <p>This service handles the creation, retrieval, and management of events.
 * It also manages event participations. All business logic related to events,
 * such as validation and time-based checks, is encapsulated here.
 * Collections returned by this service are unmodifiable to preserve encapsulation.</p>
 */
public class EventService {

    private final List<Event> eventList;
    private final List<Participation> participationList;

    /**
     * Constructs an EventService with empty internal storage.
     */
    public EventService() {
        this.eventList = new ArrayList<>();
        this.participationList = new ArrayList<>();
    }

    /**
     * Organizes (adds) a new event to the system.
     *
     * @param title       Short descriptive title.
     * @param description Detailed description.
     * @param dateTime    Date and time when the event takes place.
     * @param location    Geographical location.
     * @param organizerId Identifier of the actor organizing this event.
     * @return The newly created Event instance.
     * @throws ValidationException When any validation fails.
     */
    public Event organizeEvent(String title, String description, LocalDateTime dateTime, String location, String organizerId) {
        validateEventData(title, description, dateTime, location, organizerId);
        Event newEvent = new Event(title.trim(), description.trim(), dateTime, location.trim(), organizerId.trim());
        this.eventList.add(newEvent);
        return newEvent;
    }

    /**
     * Books a participation for an event.
     *
     * @param participation The participation to book.
     * @throws ValidationException If participation is null.
     */
    public void bookEvent(Participation participation) {
        Objects.requireNonNull(participation, "Participation cannot be null.");
        this.participationList.add(participation);
    }

    /**
     * Returns an unmodifiable list of currently stored events.
     *
     * @return Unmodifiable list of events.
     */
    public List<Event> getEventList() {
        return Collections.unmodifiableList(new ArrayList<>(this.eventList));
    }

    /**
     * Replaces the internal event list with the provided list.
     *
     * @param eventList New list of events.
     * @throws ValidationException If eventList is null.
     */
    public void setEventList(List<Event> eventList) {
        if (eventList == null) {
            throw new ValidationException("Event list cannot be null.");
        }
        this.eventList.clear();
        this.eventList.addAll(eventList);
    }

    /**
     * Returns an unmodifiable list of stored participations.
     *
     * @return Unmodifiable list of participations.
     */
    public List<Participation> getParticipationList() {
        return Collections.unmodifiableList(new ArrayList<>(this.participationList));
    }

    /**
     * Legacy/Italian-named getter preserved for compatibility.
     *
     * @return Unmodifiable list of participations.
     * @deprecated Prefer {@link #getParticipationList()}.
     */
    @Deprecated
    public List<Participation> getListaPartecipazioni() {
        return getParticipationList();
    }

    /**
     * Replaces the internal participation list with the provided list.
     *
     * @param participationList New participation list.
     * @throws ValidationException If participationList is null.
     */
    public void setParticipationList(List<Participation> participationList) {
        if (participationList == null) {
            throw new ValidationException("Participation list cannot be null.");
        }
        this.participationList.clear();
        this.participationList.addAll(participationList);
    }

    /**
     * Legacy/Italian-named setter preserved for compatibility.
     *
     * @param participationList New participation list.
     * @deprecated Prefer {@link #setParticipationList(List)}.
     */
    @Deprecated
    public void setListaPartecipazioni(List<Participation> participationList) {
        setParticipationList(participationList);
    }

    /**
     * Finds an event by its unique identifier.
     *
     * @param eventId The ID of the event to find.
     * @return An Optional containing the found event, or empty if not found.
     * @throws ValidationException If eventId is null or empty.
     */
    public Optional<Event> findEventById(String eventId) {
        validateId(eventId);
        return eventList.stream()
                .filter(event -> event.getId().equals(eventId))
                .findFirst();
    }

    /**
     * Checks whether the event is scheduled for a future date/time.
     *
     * @param event The event to check.
     * @return True when event is upcoming.
     * @throws ValidationException If event is null.
     */
    public boolean isUpcoming(Event event) {
        Objects.requireNonNull(event, "Event cannot be null.");
        return event.getDateTime() != null && event.getDateTime().isAfter(LocalDateTime.now());
    }

    /**
     * Checks whether the event has already taken place.
     *
     * @param event The event to check.
     * @return True when event is in the past.
     * @throws ValidationException If event is null.
     */
    public boolean isPast(Event event) {
        Objects.requireNonNull(event, "Event cannot be null.");
        return event.getDateTime() != null && event.getDateTime().isBefore(LocalDateTime.now());
    }

    /**
     * Validates event data parameters.
     *
     * @param title       Candidate title.
     * @param description Candidate description.
     * @param dateTime    Candidate date/time.
     * @param location    Candidate location.
     * @param organizerId Candidate organizer ID.
     * @throws ValidationException When any validation fails.
     */
    private void validateEventData(String title, String description, LocalDateTime dateTime, String location, String organizerId) {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Event title cannot be null or empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException("Event description cannot be null or empty.");
        }
        if (dateTime == null) {
            throw new ValidationException("Event date and time cannot be null.");
        }
        if (dateTime.isBefore(LocalDateTime.of(2025, 1, 1, 0, 0))) {
            throw new ValidationException("Event date cannot be before year 2025.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new ValidationException("Event location cannot be null or empty.");
        }
        if (organizerId == null || organizerId.trim().isEmpty()) {
            throw new ValidationException("Organizer ID cannot be null or empty.");
        }
    }

    /**
     * Validates that the event ID is not null or empty.
     *
     * @param id The identifier to validate.
     * @throws ValidationException If the ID is null or empty.
     */
    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("Event ID cannot be null or empty.");
        }
    }
}
