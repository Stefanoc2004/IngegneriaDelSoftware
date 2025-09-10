package it.unicam.cs.ids.filieraagricola.controllers;

import it.unicam.cs.ids.filieraagricola.model.Event;
import it.unicam.cs.ids.filieraagricola.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {


   @Autowired
   private EventService service;

    @PostMapping
    public Event organizeEvent(@RequestBody Event event) {
        return service.organizeEvent(event);
    }

    @GetMapping
    public List<Event> getEvents() {
        return service.getEvents();
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable String id) {
        return service.getEvent(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable String id) {
        return service.delete(id);
    }


}
