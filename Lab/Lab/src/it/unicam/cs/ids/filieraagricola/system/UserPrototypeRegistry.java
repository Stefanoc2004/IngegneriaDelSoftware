package it.unicam.cs.ids.filieraagricola.system;

import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.service.exception.NotFoundException;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Thread-safe registry responsible for managing {@link User} prototypes.
 *
 * <p>Keys are normalized using {@code trim() + toLowerCase()} to avoid accidental
 * case-related mismatches. Registered prototypes are stored and cloned on retrieval.</p>
 */
public class UserPrototypeRegistry {

    private final ConcurrentMap<String, User> registry;

    /**
     * Creates an empty, thread-safe registry.
     */
    public UserPrototypeRegistry() {
        this.registry = new ConcurrentHashMap<>();
    }

    /**
     * Registers a prototype under the given name.
     *
     * @param name      normalized prototype name (not null/blank)
     * @param prototype prototype instance (not null)
     * @param overwrite if true existing prototype is replaced, otherwise registration fails if name exists
     * @throws ValidationException if name is invalid, prototype is null, or already exists and overwrite==false
     */
    public void registerPrototype(String name, User prototype, boolean overwrite) {
        String key = validateAndNormalizeName(name);
        if (prototype == null) {
            throw new ValidationException("Prototype cannot be null");
        }
        if (!overwrite) {
            User prev = registry.putIfAbsent(key, prototype);
            if (prev != null) {
                throw new ValidationException("Prototype already registered for name: " + name);
            }
        } else {
            registry.put(key, prototype);
        }
    }

    /**
     * Convenience registration that overwrites existing prototypes.
     *
     * @param name      prototype name
     * @param prototype prototype
     */
    public void registerPrototype(String name, User prototype) {
        registerPrototype(name, prototype, true);
    }

    /**
     * Checks whether a prototype with the given name exists.
     *
     * @param name prototype name (must not be null/blank)
     * @return true if present, false otherwise
     * @throws ValidationException if name invalid
     */
    public boolean hasPrototype(String name) {
        String key = validateAndNormalizeName(name);
        return registry.containsKey(key);
    }

    /**
     * Returns a cloned instance of the prototype with the given name.
     *
     * @param name prototype name (must not be null/blank)
     * @return Optional containing a cloned prototype if found; otherwise Optional.empty()
     * @throws ValidationException if name invalid
     */
    public Optional<User> getClonedPrototype(String name) {
        String key = validateAndNormalizeName(name);
        User proto = registry.get(key);
        return proto == null ? Optional.empty() : Optional.of(proto.clone());
    }

    /**
     * Returns a cloned instance of the prototype with the given name or throws if not found.
     *
     * @param name prototype name (must not be null/blank)
     * @return cloned User instance
     * @throws ValidationException if name invalid
     * @throws NotFoundException   if prototype not registered
     */
    public User getPrototypeOrThrow(String name) {
        return getClonedPrototype(name)
                .orElseThrow(() -> new NotFoundException("Prototype not found: " + name));
    }

    /**
     * Returns an unmodifiable view of registered prototype names.
     *
     * @return unmodifiable set of normalized names
     */
    public Set<String> listPrototypeNames() {
        return Collections.unmodifiableSet(registry.keySet());
    }

    /**
     * Removes the prototype registered under the given name.
     *
     * @param name prototype name (must not be null/blank)
     * @return true if removed, false if nothing was registered under the name
     * @throws ValidationException if name invalid
     */
    public boolean removePrototype(String name) {
        String key = validateAndNormalizeName(name);
        return registry.remove(key) != null;
    }

    /**
     * Clears all registered prototypes.
     */
    public void clear() {
        registry.clear();
    }

    // ---------- helpers ----------

    private static String validateAndNormalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Prototype name cannot be null or empty");
        }
        return name.trim().toLowerCase();
    }
}
