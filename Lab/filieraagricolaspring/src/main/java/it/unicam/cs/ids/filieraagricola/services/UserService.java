package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.model.UserRole;
import it.unicam.cs.ids.filieraagricola.model.repositories.UserRepository;
import it.unicam.cs.ids.filieraagricola.services.exception.ValidationException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service that manages {@link User} creation, authentication and session state.
 *
 * <p>Implements the Prototype pattern by cloning registered prototypes to create
 * new user instances. Also provides simple role checks against the HTTP session.</p>
 */
@Service
public class UserService {
    public static final String USER_KEY = "user";
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserPrototypeRegistry registry;
    @Value("${testing.free.access}")
    private boolean freeAccess;


    /**
     * Convenience factory to create a reusable prototype with pre-filled permissions.
     *
     * @param permissions permissions to assign to the prototype
     * @return new prototype instance with specified permissions
     */
    public static User makePrototype(UserRole... permissions) {
        User user = new User();
        user.setPermissions(permissions);
        return user;
    }

    /**
     * Creates a new user by cloning a named prototype and customizing fields.
     *
     * @return the created user instance
     * @throws ValidationException if inputs invalid or prototype not found
     */
    public User createUser(String prototypeName, String username, String password, String email) {
        if (prototypeName == null || prototypeName.trim().isEmpty())
            throw new ValidationException("Prototype name cannot be null or empty");
        if (username == null || username.isBlank())
            throw new ValidationException("Username cannot be null or empty");
        if (password == null || password.isEmpty())
            throw new ValidationException("Password cannot be null or empty");
        if (email == null || email.isBlank())
            throw new ValidationException("Email cannot be null or empty");

        String normalizedEmail = email.trim().toLowerCase();
        // Obtain a cloned prototype (throws NotFoundException if missing)
        User newUser = registry.getPrototypeOrThrow(prototypeName);

        // customize fields
        newUser.setName(username);
        // Note: password should be hashed in production; here we accept raw string per simplified model
        newUser.setPassword(password);
        newUser.setEmail(normalizedEmail);

        // assign id using in-memory generator
        newUser.giveNewId();
        return this.repository.save(newUser);

    }

    /**
     * Registers a prototype under the provided name.
     */
    public void registerPrototype(String prototypeName, User prototype) {
        registry.registerPrototype(prototypeName, prototype);
    }

    /**
     * Authenticates a user by email and password and stores it in the session.
     */

    public Boolean authenticate(String email, String password) {
        if (email == null || email.isBlank()) throw new ValidationException("Email cannot be null or empty");
        if (password == null) throw new ValidationException("Password cannot be null");

        String needle = email.trim().toLowerCase();
        Optional<User> opt = repository.findByEmailAndPassword(needle, password);
        if (opt.isEmpty()) {
            return false;
        }
        User user = opt.get();
        httpSession.setAttribute(USER_KEY, user);
        return true;

    }

    public void logout() {
        if (httpSession.getAttribute(USER_KEY) != null) {
            httpSession.removeAttribute(USER_KEY);
        }
    }

    /**
     * Checks whether the current session user has the given role.
     */
    public Boolean hasRole(UserRole role) {
        if (freeAccess) {
            return true;
        }
        User user = (User) httpSession.getAttribute(USER_KEY);
        if (user == null) {
            return false;
        }
        UserRole[] userRoles = user.getPermissions();
        for (UserRole roles : userRoles) {
            if (roles.equals(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all users.
     */
    public List<User> getUsers() {
        return repository.findAll();
    }

}
