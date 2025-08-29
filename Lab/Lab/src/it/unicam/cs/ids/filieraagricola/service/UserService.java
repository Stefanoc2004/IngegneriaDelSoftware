package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserService {

    private List<User> userList;

    private Map<String, User> prototypes;

    public UserService() {
        this.userList = new LinkedList<>();
    }

    // Registering prototypes
    public void registerPrototype(String prototypeName, User prototype) {
        prototypes.put(prototypeName, prototype);
    }

    public void createUser(String prototypeName, String username, String password, String email) {
        User prototype = prototypes.get(prototypeName);
        User newUser = prototype.clone();
        newUser.setEmail(email);
        newUser.setName(username);
        newUser.setPassword(password);
        userList.add(newUser);
    }

    public static User makePrototype(String... permissions) {
        User user = new User(0,"","","");
        user.setPermissions(permissions);
        return user;
    }
}
