package it.unicam.cs.ids.filieraagricola.system;

import it.unicam.cs.ids.filieraagricola.model.User;

public class LoggedUser {
    private User user;

    public LoggedUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}