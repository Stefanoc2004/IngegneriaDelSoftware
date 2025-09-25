package it.unicam.cs.ids.filieraagricola.controllers;

import it.unicam.cs.ids.filieraagricola.controllers.dto.CreateSupplyChainDto;
import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.SupplyChain;
import it.unicam.cs.ids.filieraagricola.model.SupplyChainPoint;
import it.unicam.cs.ids.filieraagricola.model.UserRole;
import it.unicam.cs.ids.filieraagricola.services.SupplyChainService;
import it.unicam.cs.ids.filieraagricola.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/supplychain")
public class SupplyChainController {

    @Autowired
    UserService userService;
    @Autowired
    private SupplyChainService service;


    @PostMapping("/{supplyChainId}/products")
    public ResponseEntity<Product> acquireProduct(@RequestBody Product product, @PathVariable String supplyChainId) {
        if (!userService.hasRole(UserRole.PRODUCER)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Product p = service.acquireProduct(product, supplyChainId);
        return ResponseEntity.ok(p);

    }

    @DeleteMapping("/{supplyChainId}/products/{productsId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable String supplyChainId, @PathVariable String productsId) {
        if (!userService.hasRole(UserRole.PRODUCER)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }
        Boolean b = service.deleteProduct(supplyChainId, productsId);
        return ResponseEntity.ok(b);
    }

    @GetMapping("/{supplyChainId}/products")
    public List<Product> getProductList(@PathVariable String supplyChainId) {
        return service.getProductList(supplyChainId);
    }


    @GetMapping("")
    public List<SupplyChain> getSupplyChainList() {
        return service.getSupplyChainRepository();
    }

    @PostMapping("")
    public ResponseEntity<SupplyChain> createSupplayChain(@RequestBody CreateSupplyChainDto dto) {
        if (userService.hasRole(UserRole.PRODUCER)) {
            return ResponseEntity.ok(service.createSupplyChain(dto.getSupplyChainName(), new LinkedList<>(), new LinkedList<>()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

    }

    @GetMapping("/findSupplyChainByTheName/{name}")
    public List<SupplyChain> findSupplyChainsByName(@PathVariable String name) {
        return service.findSupplyChainsByName(name);
    }

    @GetMapping("/findProductsByCategory/{category}")
    public List<Product> findProductsByCategory(@PathVariable String category) {
        return service.findProductsByCategory(category);
    }


    @PostMapping("/{supplyChainId}/points")
    public ResponseEntity<SupplyChainPoint> acquirePoint(@RequestBody SupplyChainPoint point, @PathVariable String supplyChainId) {
        if (!userService.hasRole(UserRole.PRODUCER)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        SupplyChainPoint p = service.acquirePoint(point, supplyChainId);
        return ResponseEntity.ok(p);

    }


}
