package it.unicam.cs.ids.filieraagricola.controllers;

import it.unicam.cs.ids.filieraagricola.controllers.dto.ParticipationDto;
import it.unicam.cs.ids.filieraagricola.model.Event;
import it.unicam.cs.ids.filieraagricola.model.Participation;
import it.unicam.cs.ids.filieraagricola.model.UserRole;
import it.unicam.cs.ids.filieraagricola.services.EventService;
import it.unicam.cs.ids.filieraagricola.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    UserService userService;

    @Autowired
    private EventService service;

    @PostMapping
    public ResponseEntity<Event> organizeEvent(@RequestBody Event event) {
        //Animator
        if (!userService.hasRole(UserRole.ANIMATOR)) {
            // restituire codice http
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Event e = service.organizeEvent(event);
        return ResponseEntity.ok(e);
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
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        //Animator
        if (!userService.hasRole(UserRole.ANIMATOR)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }
        Boolean b = service.delete(id);
        return ResponseEntity.ok(b);
    }

    @GetMapping("/{eventId}/partecipations")
    public List<Participation> getPartecipation(@PathVariable String eventId) {
        return service.getPartecipations(eventId);
    }

    @GetMapping("/{eventId}/partecipations/{partecipationId}")
    public Participation getPartecipation(@PathVariable String eventId, @PathVariable String partecipationId) {
        return service.getPartecipation(partecipationId);
    }

    @DeleteMapping("/{eventId}/partecipations/{partecipationId}")
    public ResponseEntity<Boolean> deletePartecipations(@PathVariable String eventId, @PathVariable String partecipationId) {
        if (userService.hasRole(UserRole.ANIMATOR)) {
            return ResponseEntity.ok(service.deletePartecipation(partecipationId));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
    }

    @PostMapping("/{eventId}/partecipations")
    public ResponseEntity<Boolean> createPartecipation(@PathVariable String eventId, @RequestBody ParticipationDto participationDto) {
        if (userService.hasRole(UserRole.ANIMATOR)) {
            return ResponseEntity.ok(service.createPartecipation(eventId, participationDto));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
    }


}
