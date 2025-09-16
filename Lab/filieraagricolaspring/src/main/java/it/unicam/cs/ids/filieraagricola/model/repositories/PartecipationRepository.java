package it.unicam.cs.ids.filieraagricola.model.repositories;

import it.unicam.cs.ids.filieraagricola.model.Event;
import it.unicam.cs.ids.filieraagricola.model.Participation;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface PartecipationRepository extends ListCrudRepository<Participation, String> {


    List<Participation> findByEvent(Event event);
}
