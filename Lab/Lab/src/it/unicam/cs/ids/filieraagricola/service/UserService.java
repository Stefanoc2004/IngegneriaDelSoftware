package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.model.UserRole;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;
import it.unicam.cs.ids.filieraagricola.system.UserPrototypeRegistry;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service responsible for managing user accounts and prototype-based user creation.
 *
 * <p>This service stores and manages registered prototypes (by name). New users are
 * created by cloning a registered prototype and customizing fields (name, password, email).
 * The service applies defensive copying when returning collections.</p>
 *
 * <p>This in-memory service assigns incremental ids via an internal {@link AtomicInteger}.
 * In production, id assignment should be delegated to a persistence layer.</p>
 */
public class UserService {

    private final List<User> userList;
    private final UserPrototypeRegistry registry;
    private final AtomicInteger idGenerator;

    /**
     * Constructs a new UserService with a fresh prototype registry and id generator starting at 1.
     */
    public UserService() {
        this(new UserPrototypeRegistry(), 1);
    }

    /**
     * Constructs a new UserService with given prototype registry and starting id.
     *
     * @param registry    prototype registry (must not be null)
     * @param initialId   initial id to use (must be >= 0)
     * @throws NullPointerException     if registry is null
     * @throws IllegalArgumentException if initialId < 0
     */
    public UserService(UserPrototypeRegistry registry, int initialId) {
        this.userList = new LinkedList<>();
        this.registry = Objects.requireNonNull(registry, "UserPrototypeRegistry cannot be null");
        if (initialId < 0) throw new IllegalArgumentException("initialId must be >= 0");
        this.idGenerator = new AtomicInteger(initialId);
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

        // Ensure unique email (case-insensitive)
        String normalizedEmail = email.trim().toLowerCase();
        if (userList.stream()
                .anyMatch(u -> u.getEmail() != null && u.getEmail().trim().toLowerCase().equals(normalizedEmail))) {
            throw new ValidationException("Email already registered: " + email);
        }

        // Obtain a cloned prototype (throws NotFoundException if missing)
        User newUser = registry.getPrototypeOrThrow(prototypeName);

        // customize fields
        newUser.setName(username);
        // Note: password should be hashed in production; here we accept raw string per simplified model
        newUser.setPassword(password);
        newUser.setEmail(email);

        // assign id using in-memory generator
        newUser.setId(idGenerator.getAndIncrement());

        this.userList.add(newUser);

        // return defensive clone
        return newUser.clone();
    }

    /**
     * Convenience factory to create a reusable prototype with pre-filled permissions and role.
     *
     * @param role        role to assign to the prototype (not null)
     * @param permissions permission strings to assign to the prototype
     * @return new User prototype instance with specified permissions (id = 0, name/email empty)
     */
    public static User makePrototype(UserRole role, String... permissions) {
        Objects.requireNonNull(role, "Role cannot be null");
        User user = new User();
        user.setRole(role);
        user.setPermissions(permissions == null ? new String[0] : permissions.clone());
        return user;
    }

    /**
     * Returns an unmodifiable list of managed users (defensive copy).
     *
     * @return unmodifiable list of users
     */
    public List<User> getUserList() {
        List<User> copies = new ArrayList<>(userList.size());
        for (User u : userList) {
            copies.add(u.clone());
        }
        return Collections.unmodifiableList(copies);
    }

    /**
     * Authenticate a user by email and password.
     *
     * @param email user email
     * @param password raw password
     * @return Optional with the authenticated user (defensive clone) or empty if auth fails
     * @throws ValidationException if email or password null/blank
     */
    public Optional<User> authenticate(String email, String password) {
        if (email == null || email.isBlank()) throw new ValidationException("Email cannot be null or empty");
        if (password == null) throw new ValidationException("Password cannot be null");

        String needle = email.trim().toLowerCase();
        return userList.stream()
                .filter(u -> u.getEmail() != null
                        && u.getEmail().trim().toLowerCase().equals(needle)
                        && Objects.equals(u.getPassword(), password))
                .findFirst()
                .map(User::clone);
    }

    /**
     * Finds a user by email address.
     *
     * @param email email to search for (must not be null)
     * @return Optional containing the found user (defensive clone) or empty if not found
     * @throws ValidationException if email is null
     */
    public Optional<User> findUserByEmail(String email) {
        if (email == null) throw new ValidationException("Email cannot be null");
        String needle = email.trim().toLowerCase();
        return userList.stream()
                .filter(u -> u.getEmail() != null && u.getEmail().trim().toLowerCase().equals(needle))
                .findFirst()
                .map(User::clone);
    }

    /**
     * Removes a user from the managed list.
     *
     * @param user user to remove (must not be null)
     * @return true if removed, false if not found
     * @throws ValidationException if user is null
     */
    public boolean removeUser(User user) {
        if (user == null) throw new ValidationException("User cannot be null");
        return userList.remove(user);
    }

    /**
     * Returns an unmodifiable view of registered prototype names.
     *
     * @return set of registered prototype names
     */
    public Set<String> listPrototypeNames() {
        return registry.listPrototypeNames();
    }

    /**
     * Returns a defensive copy of a registered prototype by name.
     *
     * @param prototypeName name of prototype (must not be null)
     * @return Optional containing a cloned prototype or empty if not found
     * @throws ValidationException if prototypeName is null
     */
    public Optional<User> getPrototype(String prototypeName) {
        return registry.getClonedPrototype(prototypeName);
    }
}