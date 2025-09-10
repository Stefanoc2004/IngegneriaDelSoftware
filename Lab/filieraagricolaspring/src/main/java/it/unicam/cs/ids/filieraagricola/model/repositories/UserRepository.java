package it.unicam.cs.ids.filieraagricola.model.repositories;

import it.unicam.cs.ids.filieraagricola.model.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, String> {


    Optional<User> findByEmailAndPassword(String email, String password);












}
