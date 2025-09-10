package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.model.Event;
import it.unicam.cs.ids.filieraagricola.model.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

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



}
