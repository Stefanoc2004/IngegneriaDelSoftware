package it.unicam.cs.ids.filieraagricola.model.repositories;

import it.unicam.cs.ids.filieraagricola.model.Content;
import it.unicam.cs.ids.filieraagricola.model.ContentState;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ContentRepository extends ListCrudRepository<Content, String> {

    List<Content> findByState(ContentState state);



}
