package it.unicam.cs.ids.filieraagricola.controllers;

import it.unicam.cs.ids.filieraagricola.controllers.dto.CreateContentDto;
import it.unicam.cs.ids.filieraagricola.model.Content;
import it.unicam.cs.ids.filieraagricola.model.ContentState;
import it.unicam.cs.ids.filieraagricola.model.UserRole;
import it.unicam.cs.ids.filieraagricola.services.ContentService;
import it.unicam.cs.ids.filieraagricola.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contents")
public class ContentsController {

    @Autowired
    private ContentService service;
    @Autowired
    private UserService userService;


    @GetMapping("")
    public ResponseEntity<List<Content>> findAll() {
        if (userService.hasRole(UserRole.TRANSFORMER)
                || userService.hasRole(UserRole.DISTRIBUTOR)
                || userService.hasRole(UserRole.CURATOR)) {
            return ResponseEntity.ok(service.getContents());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @GetMapping("/approved")
    public List<Content> findApproved() {
        return service.getContents(ContentState.APPROVED);
    }

    @GetMapping("/rejected")
    public ResponseEntity<List<Content>> findRejected() {
        if (userService.hasRole(UserRole.TRANSFORMER)
                || userService.hasRole(UserRole.DISTRIBUTOR)
                || userService.hasRole(UserRole.CURATOR)) {
            return ResponseEntity.ok(service.getContents(ContentState.REJECTED));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Content>> findPending() {
        if (userService.hasRole(UserRole.TRANSFORMER)
                || userService.hasRole(UserRole.DISTRIBUTOR)
                || userService.hasRole(UserRole.CURATOR)) {
            return ResponseEntity.ok(service.getContents(ContentState.PENDING));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }


    @PostMapping("")
    public ResponseEntity<Content> create(@RequestBody CreateContentDto dto) {
        if (userService.hasRole(UserRole.TRANSFORMER)
                || userService.hasRole(UserRole.DISTRIBUTOR)
                || userService.hasRole(UserRole.CURATOR)) {
            return ResponseEntity.ok(service.addContent(dto.getName(), dto.getDescription(), dto.getType(), dto.getIdSupplyChainPoint()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

    }

    @GetMapping("/{id}")
    public Content getContent(@PathVariable String id) {
        return service.getContent(id);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<Boolean> approveContent(@PathVariable String id) {
        if (!userService.hasRole(UserRole.CURATOR)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);

        }
        return ResponseEntity.ok(service.approve(id));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable String id) {
        return service.removeContent(id);
    }


}
