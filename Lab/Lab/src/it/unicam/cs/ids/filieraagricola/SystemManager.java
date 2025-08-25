package it.unicam.cs.ids.filieraagricola;

public class SystemManager {
    private static SystemManager instance;
    private LoggedUser loggedUser; // current session state

    // Private constructor -> singleton
    private SystemManager() {}

    public static SystemManager getInstance() {
        if (instance == null) {
            instance = new SystemManager();
        }
        return instance;
    }

    // Handles a request
    public void handleRequest(FormatRequest request) {
        switch (request.getMethod()) {
            case "login":
                if (request.getParams().length == 2) {
                    login(request.getParams()[0], request.getParams()[1]);
                } else {
                    System.out.println("Insufficient parameters for login");
                }
                break;

            case "anonymousLogin":
                anonymousLogin();
                break;

            default:
                System.out.println("Unknown method: " + request.getMethod());
        }
    }

    public void login(String name, String password) {
        // In the future, validate against a UserService
        User u = new User(1, name, password, name + "@mail.com");
        this.loggedUser = new LoggedUser(u);

        System.out.println("Login successful for: " + u.getName());
    }

    public void anonymousLogin() {
        User u = new User(0, "Anonymous", "", "");
        this.loggedUser = new LoggedUser(u);

        System.out.println("Anonymous login successful.");
    }

    public LoggedUser getLoggedUser() {
        return loggedUser;
    }
}