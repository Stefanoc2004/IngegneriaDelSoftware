package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.model.Prototype;

import java.util.*;

/**
 * Service responsible for managing user accounts and prototype-based user creation.
 *
 * <p>This service stores and manages registered prototypes (by name). New users can be
 * created by cloning a registered prototype and customizing fields (username, password, email).
 * The service applies defensive copying when returning collections.</p>
 *
 * <p>All public methods validate inputs and throw IllegalArgumentException for invalid parameters.</p>
 */
public class UserService {

    private final List<User> userList;
    private final Map<String, User> prototypes;

    /**
     * Constructs a new UserService with empty user storage and prototype registry.
     */
    public UserService() {
        this.userList = new LinkedList<>();
        this.prototypes = new HashMap<>();
    }

    /**
     * Registers a prototype under the provided name. The prototype is stored as-is;
     * it will be cloned when used to create new users.
     *
     * @param prototypeName unique name for the prototype (must not be null/empty)
     * @param prototype     prototype User instance (must not be null)
     * @throws IllegalArgumentException if inputs invalid
     */
    public void registerPrototype(String prototypeName, User prototype) {
        if (prototypeName == null || prototypeName.trim().isEmpty())
            throw new IllegalArgumentException("Prototype name cannot be null or empty");
        if (prototype == null) throw new IllegalArgumentException("Prototype cannot be null");
        this.prototypes.put(prototypeName.trim(), prototype);
    }

    /**
     * Creates a new user by cloning the named prototype and customizing its fields.
     *
     * @param prototypeName name of the registered prototype (must exist)
     * @param username      display name for the new user (must not be null/empty)
     * @param password      password for the new user (must not be null/empty)
     * @param email         email for the new user (must not be null/empty)
     * @throws IllegalArgumentException if inputs invalid or prototype not found
     */
    public void createUser(String prototypeName, String username, String password, String email) {
        if (prototypeName == null || prototypeName.trim().isEmpty())
            throw new IllegalArgumentException("Prototype name cannot be null or empty");
        Objects.requireNonNull(username, "Username cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");

        User prototype = prototypes.get(prototypeName.trim());
        if (prototype == null) {
            throw new IllegalArgumentException("Prototype not found: " + prototypeName);
        }

        // Clone the prototype (Prototype pattern)
        User newUser = prototype.clone();

        newUser.setName(username);
        newUser.setPassword(password);
        newUser.setEmail(email);

        this.userList.add(newUser);
    }

    /**
     * Convenience factory to create a reusable prototype with pre-filled permissions.
     *
     * @param permissions permission strings to assign to the prototype
     * @return new User prototype instance with specified permissions (id = 0, name/email empty)
     */
    public static User makePrototype(String... permissions) {
        User user = new User();
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
     * Finds a user by email address.
     *
     * @param email email to search for (must not be null)
     * @return Optional containing the found user (defensive clone) or empty if not found
     * @throws IllegalArgumentException if email is null
     */
    public Optional<User> findUserByEmail(String email) {
        if (email == null) throw new IllegalArgumentException("Email cannot be null");
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
     * @throws IllegalArgumentException if user is null
     */
    public boolean removeUser(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        return userList.remove(user);
    }

    /**
     * Returns an unmodifiable view of registered prototype names.
     *
     * @return set of registered prototype names
     */
    public Set<String> listPrototypeNames() {
        return Collections.unmodifiableSet(prototypes.keySet());
    }

    /**
     * Returns a defensive copy of a registered prototype by name.
     *
     * @param prototypeName name of prototype (must not be null)
     * @return Optional containing a cloned prototype or empty if not found
     * @throws IllegalArgumentException if prototypeName is null
     */
    public Optional<User> getPrototype(String prototypeName) {
        if (prototypeName == null) throw new IllegalArgumentException("Prototype name cannot be null");
        User proto = prototypes.get(prototypeName.trim());
        if (proto == null) return Optional.empty();
        return Optional.of(proto.clone());
    }
}