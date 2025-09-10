package it.unicam.cs.ids.filieraagricola.controllers;

import it.unicam.cs.ids.filieraagricola.controllers.dto.AuthenticateDto;
import it.unicam.cs.ids.filieraagricola.controllers.dto.CreateUserDto;
import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody CreateUserDto createUserDto) {
        return userService.createUser(createUserDto.getPrototypeName(), createUserDto.getUsername(), createUserDto.getPassword(), createUserDto.getEmail());
    }

    @PostMapping("/login")
    public Boolean authenticate(@RequestBody AuthenticateDto authenticateDto) {
        return userService.authenticate(authenticateDto.getEmail(), authenticateDto.getPassword());
    }

}
