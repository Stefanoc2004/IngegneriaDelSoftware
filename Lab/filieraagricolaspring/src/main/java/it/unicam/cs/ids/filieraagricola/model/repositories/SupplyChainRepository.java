package it.unicam.cs.ids.filieraagricola.model.repositories;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.SupplyChain;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface SupplyChainRepository extends ListCrudRepository<SupplyChain, String> {

    public List<SupplyChain> findByName(String name);


}
