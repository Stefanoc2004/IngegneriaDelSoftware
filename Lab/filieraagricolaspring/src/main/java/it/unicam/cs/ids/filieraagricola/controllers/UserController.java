package it.unicam.cs.ids.filieraagricola.controllers;

import it.unicam.cs.ids.filieraagricola.controllers.dto.AuthenticateDto;
import it.unicam.cs.ids.filieraagricola.controllers.dto.CreateUserDto;
import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.model.UserRole;
import it.unicam.cs.ids.filieraagricola.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {
        if (!userService.hasRole(UserRole.PLATFORM_MANAGER)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        User u = userService.createUser(createUserDto.getPrototypeName(), createUserDto.getUsername(), createUserDto.getPassword(), createUserDto.getEmail());

        return ResponseEntity.ok(u);
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> authenticate(@RequestBody AuthenticateDto authenticateDto) {
        Boolean b = userService.authenticate(authenticateDto.getEmail(), authenticateDto.getPassword());
        if (b) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }

    @PostMapping("/logout")
    public String logout() {
        userService.logout();
        return "done";
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        if (!(userService.hasRole(UserRole.ANIMATOR) || userService.hasRole(UserRole.PLATFORM_MANAGER))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LinkedList());
        }
        List<User> u = userService.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }

}
