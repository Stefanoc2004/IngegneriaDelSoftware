package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.model.UserRole;
import it.unicam.cs.ids.filieraagricola.model.repositories.UserRepository;
import it.unicam.cs.ids.filieraagricola.services.exception.ValidationException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
     * Convenience factory to create a reusable prototype with pre-filled permissions and role.
     *
     * @param permissions permission strings to assign to the prototype
     * @return new User prototype instance with specified permissions (id = 0, name/email empty)
     */
    public static User makePrototype(UserRole... permissions) {
        User user = new User();
        user.setPermissions(permissions);
        return user;
    }

    /**
     * Creates a new user by cloning the named prototype and customizing its fields.
     *
     * @param prototypeName name of the registered prototype (must exist)
     * @param username      display name for the new user (must not be null/empty)
     * @param password      password for the new user (must not be null/empty)
     * @param email         email for the new user (must not be null/empty and unique)
     * @return the created User instance (defensive clone)
     * @throws ValidationException if inputs invalid or prototype not found or email already used
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
     * Registers a prototype under the provided name. The prototype is stored as-is;
     * it will be cloned when used to create new users.
     *
     * @param prototypeName unique name for the prototype (must not be null/empty)
     * @param prototype     prototype User instance (must not be null)
     * @throws ValidationException if inputs invalid
     */
    public void registerPrototype(String prototypeName, User prototype) {
        registry.registerPrototype(prototypeName, prototype);
    }

    /**
     * Authenticate a user by email and password.
     *
     * @param email    user email
     * @param password raw password
     * @return Optional with the authenticated user (defensive clone) or empty if auth fails
     * @throws ValidationException if email or password null/blank
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

    public List<User> getUsers() {
        return repository.findAll();
    }

}
