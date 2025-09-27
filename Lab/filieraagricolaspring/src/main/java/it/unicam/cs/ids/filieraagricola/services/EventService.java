package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.controllers.dto.ParticipationDto;
import it.unicam.cs.ids.filieraagricola.model.Event;
import it.unicam.cs.ids.filieraagricola.model.Participation;
import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.model.repositories.EventRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.ParticipationRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service for managing {@link Event} and related {@link Participation}.
 *
 * <p>Provides creation, retrieval and deletion of events and participations using
 * Spring Data repositories.</p>
 */
@Service
public class EventService {
    @Autowired
    private ParticipationRepository participationRepository;
    @Autowired
    private EventRepository repository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Assigns a new id and persists the event.
     */
    public Event organizeEvent(Event event) {
        event.giveNewId();
        return repository.save(event);
    }

    /** Returns all events. */
    public List<Event> getEvents() {
        return repository.findAll();
    }



    /** Returns an event by id or null if not found. */
    public Event getEvent(String id) {
        Optional<Event> opt = repository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }
    /** Deletes an event by id if it exists. */
    public boolean delete(String id) {
        Event event = getEvent(id);
        if (event != null) {
            repository.delete(event);
            return true;
        }
        return false;
    }

    /** Returns participations for a given event id, or empty list if event missing. */
    public List<Participation> getPartecipations(String eventId) {
        Optional<Event> opt = repository.findById(eventId);
        if (opt.isEmpty()) {
            return new LinkedList<>();
        }
        return participationRepository.findByEvent(opt.get());
    }


    /** Returns a participation by id or null if not found. */
    public Participation getPartecipation(String id) {
        Optional<Participation> opt = participationRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }
    /** Deletes a participation by id if it exists. */
    public boolean deletePartecipation(String id) {
        Participation participation = getPartecipation(id);
        if (participation != null) {
            participationRepository.delete(participation);
            return true;
        }
        return false;
    }


    /** Creates a new participation for the given event from a DTO. */
    public boolean createPartecipation(String eventId, ParticipationDto participationDto) {

        Optional<Event> opt = repository.findById(eventId);
        if (opt.isEmpty()) {
            return false;
        }
        Optional<User> optActor = userRepository.findById(participationDto.getActorId());
        if (optActor.isEmpty()) {
            return false;
        }

        Participation participation = new Participation();
        participation.setId(UUID.randomUUID().toString());
        participation.setEvent(opt.get());
        participation.setActor(optActor.get());
        participation.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
        participation.setRole(participationDto.getRole());
        participationRepository.save(participation);
        return true;

    }

}
