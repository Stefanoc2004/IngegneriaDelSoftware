package it.unicam.cs.ids.filieraagricola.controllers;

import it.unicam.cs.ids.filieraagricola.controllers.dto.CreateContentDto;
import it.unicam.cs.ids.filieraagricola.model.Content;
import it.unicam.cs.ids.filieraagricola.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contents")
public class ContentsController {

    @Autowired
    private ContentService service;

    @GetMapping("")
    public List<Content> findAll() {
        return service.getCertifications();
    }

    @PostMapping("")
    public void create(@RequestBody CreateContentDto dto) {
        service.addContent(dto.getName(), dto.getDescription(),dto.getType());
    }


}
