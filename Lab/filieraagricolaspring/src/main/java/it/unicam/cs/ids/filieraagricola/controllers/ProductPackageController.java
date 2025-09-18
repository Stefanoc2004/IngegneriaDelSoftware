package it.unicam.cs.ids.filieraagricola.controllers;


import it.unicam.cs.ids.filieraagricola.controllers.dto.ProductPackageDTO;
import it.unicam.cs.ids.filieraagricola.model.Event;
import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.ProductPackage;
import it.unicam.cs.ids.filieraagricola.model.UserRole;
import it.unicam.cs.ids.filieraagricola.services.ProductPackageService;
import it.unicam.cs.ids.filieraagricola.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/packages")
public class ProductPackageController {

    @Autowired
    UserService userService;


    @Autowired
    private ProductPackageService productPackageService;

    @GetMapping
    public List<ProductPackage> findAll() {
       return productPackageService.findAll();
    }

    @GetMapping("/{id}")
    public ProductPackage findById(@PathVariable String id) {
       return productPackageService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        if (!userService.hasRole(UserRole.DISTRIBUTOR)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }
        Boolean b = productPackageService.delete(id);
        return ResponseEntity.ok(b);
    }

    @PostMapping()
    public ResponseEntity<ProductPackage> create(@RequestBody ProductPackageDTO dto) {
        if (!userService.hasRole(UserRole.DISTRIBUTOR)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        ProductPackage pp = productPackageService.create(dto);
        return ResponseEntity.ok(pp);
    }


}
