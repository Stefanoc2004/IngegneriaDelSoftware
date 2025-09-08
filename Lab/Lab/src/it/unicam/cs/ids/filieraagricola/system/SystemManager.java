package it.unicam.cs.ids.filieraagricola.system;

import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.service.CertificationService;
import it.unicam.cs.ids.filieraagricola.service.ContentService;
import it.unicam.cs.ids.filieraagricola.service.UserService;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
    private final Map<String, SystemAction> systemActions = new HashMap<>();
    private ContentService contentService;
    private UserService userService;
    private CertificationService certificationService;

    private SystemManager(UserService userService,
                          ContentService contentService,
                          CertificationService certificationService) {
        this.userService = Objects.requireNonNull(userService, "UserService cannot be null");
        this.contentService = Objects.requireNonNull(contentService, "ContentService cannot be null");
        this.certificationService = Objects.requireNonNull(certificationService, "CertificationService cannot be null");
    }

    /**
     * Initialize singleton with required services. Call once at startup.
     */
    public static synchronized void initialize(UserService userService,
                                               ContentService contentService,
                                               CertificationService certificationService) {
        if (instance != null) {
            throw new IllegalStateException("SystemManager already initialized");
        }
        instance = new SystemManager(userService, contentService, certificationService);
    }

    // Static method to get the Instance
    public static SystemManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SystemManager not initialized. Call initialize(...) first.");
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

    /**
     * Attempt login using UserService.authenticate.
     *
     * @param email user's email
     * @param password user's password
     */
    public void login(String email, String password) {
        Optional<User> auth = userService.authenticate(email, password);
        if (auth.isEmpty()) {
            throw new ValidationException("Invalid credentials");
        }
        this.loggedUser = new LoggedUser(auth.get());
        System.out.println("Login successful for: " + auth.get().getName());
    }

    /**
     * Anonymous login: create or get an anonymous user (no persistence).
     */
    public void anonymousLogin() {
        User anon = new User(); // no-arg constructor
        anon.setId(0);
        anon.setName("Anonymous");
        anon.setPassword("");
        anon.setEmail("");
        this.loggedUser = new LoggedUser(anon);
        System.out.println("Anonymous login successful.");
    }

    public LoggedUser getLoggedUser() {
        return loggedUser;
    }

    public ContentService getContentService() {
        return contentService;
    }

    public UserService getUserService() { return userService;}

    public String[] getPermissions() {
        return this.loggedUser.getUser().getPermissions();
    }

    public CertificationService getCertificationService() { return certificationService; }
}