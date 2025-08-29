package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Event;
import it.unicam.cs.ids.filieraagricola.model.Participation;
import it.unicam.cs.ids.filieraagricola.model.Prototype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service responsible for managing events and participations.
 *
 * <p>This service stores defensive copies of mutable domain objects when possible.
 * If a domain object implements {@link Prototype}, a {@code clone()} is performed
 * before storing or returning instances. All public methods return defensive views
 * to preserve encapsulation.</p>
 */
public class EventService {

    private List<Event> eventList;
    private List<Participation> participationList;

    /**
     * Constructs an EventService with empty internal storage.
     */
    public EventService() {
        this.eventList = new ArrayList<>();
        this.participationList = new ArrayList<>();
    }

    /**
     * Organizes (adds) a new event to the system.
     * If {@code event} implements {@link Prototype} the stored instance is a clone
     * to prevent external mutations; otherwise the instance is stored as provided.
     *
     * @param event event to organize; must not be null
     * @throws IllegalArgumentException if event is null
     */
    public void organizeEvent(Event event) {
        requireNonNullEvent(event);
        this.eventList.add(copyEvent(event));
    }

    /**
     * Books a participation for an event.
     * If {@code participation} implements {@link Prototype} a cloned instance is stored.
     *
     * @param participation participation to store; must not be null
     * @throws IllegalArgumentException if participation is null
     */
    public void bookEvent(Participation participation) {
        requireNonNullParticipation(participation);
        this.participationList.add(copyParticipation(participation));
    }

    /**
     * Returns a defensive list of managed events. Each element is a clone if the
     * original type implements {@link Prototype}; otherwise the original instance
     * is returned (assumed immutable).
     *
     * @return unmodifiable list of events (defensive copy)
     */
    public List<Event> getEventList() {
        List<Event> copies = this.eventList.stream()
                .map(this::copyEvent)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(copies);
    }

    /**
     * Replaces the internal event list with a defensive copy of the provided list.
     * Each element is cloned if it implements {@link Prototype}.
     *
     * @param eventList new event list (must not be null)
     * @throws IllegalArgumentException if eventList is null
     */
    public void setEventList(List<Event> eventList) {
        if (eventList == null) throw new IllegalArgumentException("Event list cannot be null");
        this.eventList = eventList.stream()
                .map(this::copyEvent)
                .collect(Collectors.toList());
    }

    /**
     * Returns a defensive list of participations. Each element is cloned if the
     * original type implements {@link Prototype}; otherwise the original instance
     * is returned (assumed immutable).
     *
     * @return unmodifiable list of participations (defensive copy)
     */
    public List<Participation> getParticipationList() {
        List<Participation> copies = this.participationList.stream()
                .map(this::copyParticipation)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(copies);
    }

    /**
     * Legacy/Italian-named getter preserved for compatibility.
     *
     * @return unmodifiable list of participations (defensive copy)
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
        if (participationList == null)
            throw new IllegalArgumentException("Participation list cannot be null");
        this.participationList = participationList.stream()
                .map(this::copyParticipation)
                .collect(Collectors.toList());
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
     * Returns a defensive copy of the provided Event if it implements Prototype,
     * otherwise returns the original instance (assumed immutable).
     *
     * @param event event to copy
     * @return cloned or original event
     */
    private Event copyEvent(Event event) {
        if (event instanceof Prototype) {
            // clone() returns Prototype; cast to Event (covariant clone allowed in implementations)
            return (Event) ((Prototype) event).clone();
        }
        return event;
    }

    /**
     * Returns a defensive copy of the provided Participation if it implements Prototype,
     * otherwise returns the original instance (assumed immutable).
     *
     * @param participation participation to copy
     * @return cloned or original participation
     */
    private Participation copyParticipation(Participation participation) {
        if (participation instanceof Prototype) {
            return (Participation) ((Prototype) participation).clone();
        }
        return participation;
    }

    /**
     * Validates that the provided Event is not null.
     *
     * @param event event to validate
     * @throws IllegalArgumentException if event is null
     */
    private void requireNonNullEvent(Event event) {
        if (event == null) throw new IllegalArgumentException("Event cannot be null");
    }

    /**
     * Validates that the provided Participation is not null.
     *
     * @param participation participation to validate
     * @throws IllegalArgumentException if participation is null
     */
    private void requireNonNullParticipation(Participation participation) {
        if (participation == null) throw new IllegalArgumentException("Participation cannot be null");
    }
}