package it.unicam.cs.ids.filieraagricola.system;

import it.unicam.cs.ids.filieraagricola.service.ContentService;

import java.util.HashMap;
import java.util.Map;

/**
 * SystemManager class:
 * - implemented as a Singleton (only one instance exists),
 * - manages the current logged user (session state),
 * - handles incoming requests by dispatching them to the correct action,
 * - supports login with credentials or anonymous login,
 * - provides access to the currently logged user.
 */

public class SystemManager {
    private static SystemManager instance; // unique global instance
    private LoggedUser loggedUser; // current session state
    private Map<String, SystemAction> systemActions;
    private ContentService contentService;

    // Private constructor -> singleton
    private SystemManager() {
        systemActions = new HashMap<>();
        contentService = new ContentService();
    }

    // Static method to get the Instance
    public static SystemManager getInstance() {
        if (instance == null) {
            instance = new SystemManager();
        }
        return instance;
    }

    public void addMap(String key, SystemAction systemAction) {
        systemActions.put(key, systemAction);
    }

    // Handles a request and check for the login
    public void handleRequest(FormatRequest request) {
        if (!systemActions.containsKey(request.getMethod())) {
            //aggiungi eccezione
            return;
        }
        SystemAction action = systemActions.get(request.getMethod());
        action.handleRequest(request);

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

    public ContentService getContentService() {
        return contentService;
    }

    public String[] getPermissions() {
        return this.loggedUser.getUser().getPermissions();
    }
}