package it.unicam.cs.ids.filieraagricola;

public class LoggedUser {
    private User user;

    public LoggedUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}