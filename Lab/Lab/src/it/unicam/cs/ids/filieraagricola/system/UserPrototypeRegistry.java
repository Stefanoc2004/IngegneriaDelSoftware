package it.unicam.cs.ids.filieraagricola.system;

import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.service.exception.NotFoundException;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;

import java.util.*;

/**
 * Registry responsible for managing {@link User} prototypes.
 *
 * <p>This class centralizes the storage and retrieval of user prototypes, enabling
 * creation of new users via the Prototype pattern by cloning registered prototypes.
 * It validates inputs rigorously and throws centralized domain exceptions.</p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 *   <li>Register prototypes by unique name</li>
 *   <li>Retrieve clones of registered prototypes</li>
 *   <li>List registered prototype names</li>
 *   <li>Validate inputs consistently (throwing {@link ValidationException})</li>
 *   <li>Throw {@link NotFoundException} when a requested prototype is missing</li>
 * </ul>
 */
public class UserPrototypeRegistry {

    private final Map<String, User> registry;

    /**
     * Creates an empty registry.
     */
    public UserPrototypeRegistry() {
        this.registry = new HashMap<>();
    }

    /**
     * Registers a prototype under a given name. Overrides any existing prototype with the same name.
     *
     * @param name      unique prototype name (must not be null/blank)
     * @param prototype prototype instance (must not be null)
     * @throws ValidationException if any argument is invalid
     */
    public void registerPrototype(String name, User prototype) {
        String key = normalizeName(name);
        if (prototype == null) {
            throw new ValidationException("Prototype cannot be null");
        }
        registry.put(key, prototype);
    }

    /**
     * Checks whether a prototype with the given name exists.
     *
     * @param name prototype name (must not be null/blank)
     * @return true if present, false otherwise
     * @throws ValidationException if name is invalid
     */
    public boolean hasPrototype(String name) {
        String key = normalizeName(name);
        return registry.containsKey(key);
    }

    /**
     * Returns a cloned instance of the prototype with the given name, wrapped in an Optional.
     *
     * @param name prototype name (must not be null/blank)
     * @return Optional containing a cloned prototype if found; otherwise Optional.empty()
     * @throws ValidationException if name is invalid
     */
    public Optional<User> getClonedPrototype(String name) {
        String key = normalizeName(name);
        User proto = registry.get(key);
        return proto == null ? Optional.empty() : Optional.of(proto.clone());
    }

    /**
     * Returns a cloned instance of the prototype with the given name, or throws if not found.
     *
     * @param name prototype name (must not be null/blank)
     * @return cloned User instance
     * @throws ValidationException if name is invalid
     * @throws NotFoundException   if prototype is not registered
     */
    public User getPrototypeOrThrow(String name) {
        return getClonedPrototype(name)
                .orElseThrow(() -> new NotFoundException("Prototype not found: " + name));
    }

    /**
     * Returns an unmodifiable view of all registered prototype names.
     *
     * @return unmodifiable set of names
     */
    public Set<String> listPrototypeNames() {
        return Collections.unmodifiableSet(registry.keySet());
    }

    /**
     * Removes all prototypes from the registry.
     */
    public void clear() {
        registry.clear();
    }

    // --------------- helpers ---------------

    private static String normalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Prototype name cannot be null or empty");
        }
        return name.trim();
    }
}
