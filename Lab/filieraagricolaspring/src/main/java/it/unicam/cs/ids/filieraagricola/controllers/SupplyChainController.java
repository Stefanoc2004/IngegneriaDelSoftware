package it.unicam.cs.ids.filieraagricola.controllers;

import it.unicam.cs.ids.filieraagricola.controllers.dto.CreateSupplyChainDto;
import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.SupplyChain;
import it.unicam.cs.ids.filieraagricola.services.SupplyChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/supplychain")
public class SupplyChainController {

    @Autowired
    private SupplyChainService service;


    @PostMapping("/{supplyChainId}/products")
    public Product acquireProduct(@RequestBody Product product, @PathVariable String supplyChainId) {
        return service.acquireProduct(product, supplyChainId);

    }

    @DeleteMapping("/{supplyChainId}/products/{productsId}")
    public boolean deleteProduct(@PathVariable String supplyChainId,@PathVariable String productsId) {
        //TODO
        return service.deleteProduct(null);
    }

    @GetMapping("/{supplyChainId}/products")
    public List<Product> getProductList(@PathVariable String supplyChainId) {
        return service.getProductList(supplyChainId);
    }


    @GetMapping("")
    public List<SupplyChain> getSupplyChainList() {
        return service.getSupplyChainList();
    }

    @PostMapping("")
    public SupplyChain createSupplayChain(@RequestBody CreateSupplyChainDto dto) {
        return service.createSupplyChain(dto.getSupplyChainName(), new LinkedList<>());
    }

    @GetMapping("/findSupplyChainByTheName/{name}")
    public List<SupplyChain> findSupplyChainsByName(@PathVariable String name) {
       return service.findSupplyChainsByName(name);
    }

    @GetMapping("/findProductsByCategory/{category}")
    public List<Product> findProductsByCategory(@PathVariable String category) {
        return service.findProductsByCategory(category);
    }




}
