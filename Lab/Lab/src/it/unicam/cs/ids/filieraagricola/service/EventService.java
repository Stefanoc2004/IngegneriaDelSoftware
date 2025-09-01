package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Event;
import it.unicam.cs.ids.filieraagricola.model.Participation;
import it.unicam.cs.ids.filieraagricola.model.Prototype;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service responsible for managing events and participations.
 *
 * <p>This service applies defensive copying when possible: if an object implements
 * {@link Prototype} it will be cloned (via {@code clone()}) before being stored
 * or returned. Collections returned by this service are unmodifiable to preserve
 * encapsulation.</p>
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
     * Organizes (adds) a new event to the system. A defensive copy is stored
     * if the provided event implements {@link Prototype}.
     *
     * @param event event to organize (must not be null)
     * @throws IllegalArgumentException if event is null
     */
    public void organizeEvent(Event event) {
        Objects.requireNonNull(event, "Event cannot be null");
        this.eventList.add(copyEvent(event));
    }

    /**
     * Books a participation for an event. A defensive copy is stored if the
     * provided participation implements {@link Prototype}.
     *
     * @param participation participation to book (must not be null)
     * @throws IllegalArgumentException if participation is null
     */
    public void bookEvent(Participation participation) {
        Objects.requireNonNull(participation, "Participation cannot be null");
        this.participationList.add(copyParticipation(participation));
    }

    /**
     * Returns an unmodifiable list of currently stored events.
     * Each returned element is a clone when possible.
     *
     * @return unmodifiable list of events (defensive copies when applicable)
     */
    public List<Event> getEventList() {
        return this.eventList.stream()
                .map(this::copyEvent)
                .toList();
    }

    /**
     * Replaces the internal event list with a defensive copy of the provided list.
     * Each element is cloned if it implements {@link Prototype}.
     *
     * @param eventList new list of events (must not be null)
     * @throws IllegalArgumentException if eventList is null
     */
    public void setEventList(List<Event> eventList) {
        if (eventList == null) throw new IllegalArgumentException("Event list cannot be null");
        this.eventList.clear();
        this.eventList.addAll(eventList.stream()
                .map(this::copyEvent)
                .toList());
    }

    /**
     * Returns an unmodifiable list of stored participations.
     * Each returned element is a clone when possible.
     *
     * @return unmodifiable list of participations (defensive copies when applicable)
     */
    public List<Participation> getParticipationList() {
        return this.participationList.stream()
                .map(this::copyParticipation)
                .toList();
    }

    /**
     * Legacy/Italian-named getter preserved for compatibility.
     *
     * @return unmodifiable list of participations (defensive copies when applicable)
     * @deprecated prefer {@link #getParticipationList()}
     */
    @Deprecated
    public List<Participation> getListaPartecipazioni() {
        return getParticipationList();
    }

    /**
     * Replaces the internal participation list with a defensive copy of the provided list.
     * Each element is cloned if it implements {@link Prototype}.
     *
     * @param participationList new participation list (must not be null)
     * @throws IllegalArgumentException if participationList is null
     */
    public void setParticipationList(List<Participation> participationList) {
        if (participationList == null) throw new IllegalArgumentException("Participation list cannot be null");
        this.participationList.clear();
        this.participationList.addAll(participationList.stream()
                .map(this::copyParticipation)
                .toList());
    }

    /**
     * Legacy/Italian-named setter preserved for compatibility.
     *
     * @param participationList new participation list
     * @deprecated prefer {@link #setParticipationList(List)}
     */
    @Deprecated
    public void setListaPartecipazioni(List<Participation> participationList) {
        setParticipationList(participationList);
    }

    // ----------------- private helpers -----------------

    /**
     * Returns a defensive copy of the provided Event if it implements {@link Prototype};
     * otherwise returns the original instance (assumed immutable).
     *
     * @param event event to copy
     * @return cloned or original event
     */
    private Event copyEvent(Event event) {
        if (event instanceof Prototype<?> prototype) {
            return (Event) prototype.clone();
        }
        return event;
    }

    /**
     * Returns a defensive copy of the provided Participation if it implements {@link Prototype};
     * otherwise returns the original instance (assumed immutable).
     *
     * @param participation participation to copy
     * @return cloned or original participation
     */
    private Participation copyParticipation(Participation participation) {
        if (participation instanceof Prototype<?> prototype) {
            return (Participation) prototype.clone();
        }
        return participation;
    }
}