package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.controllers.dto.PartecipationDto;
import it.unicam.cs.ids.filieraagricola.model.Event;
import it.unicam.cs.ids.filieraagricola.model.Participation;
import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.model.repositories.EventRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.PartecipationRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.UserRepository;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {
    @Autowired
    private PartecipationRepository partecipationRepository;
    @Autowired
    private EventRepository repository;
    @Autowired
    private UserRepository userRepository;

    public Event organizeEvent(Event event) {
        event.giveNewId();
        return repository.save(event);
    }

    public List<Event> getEvents() {
        return repository.findAll();
    }



    public Event getEvent(String id) {
        Optional<Event> opt = repository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }
    public boolean delete(String id) {
        Event event = getEvent(id);
        if (event != null) {
            repository.delete(event);
            return true;
        }
        return false;
    }

    public List<Participation> getPartecipations(String eventId) {
        Optional<Event> opt = repository.findById(eventId);
        if (opt.isEmpty()) {
            return new LinkedList<>();
        }
        return partecipationRepository.findByEvent(opt.get());
    }


    public Participation getPartecipation(String id) {
        Optional<Participation> opt = partecipationRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }
    public boolean deletePartecipation(String id) {
        Participation participation = getPartecipation(id);
        if (participation != null) {
            partecipationRepository.delete(participation);
            return true;
        }
        return false;
    }


    public boolean createPartecipation(String eventId, PartecipationDto partecipationDto) {

        Optional<Event> opt = repository.findById(eventId);
        if (opt.isEmpty()) {
            return false;
        }
        Optional<User> optActor = userRepository.findById(partecipationDto.getActorId());
        if (optActor.isEmpty()) {
            return false;
        }

        Participation participation = new Participation();
        participation.setId(UUID.randomUUID().toString());
        participation.setEvent(opt.get());
        participation.setActor(optActor.get());
        participation.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
        participation.setRole(partecipationDto.getRole());
        partecipationRepository.save(participation);
        return true;

    }

}
