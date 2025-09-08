package it.unicam.cs.ids.filieraagricola.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a user (actor) of the agricultural supply-chain platform.
 *
 * <p>This class implements the Prototype pattern via {@link Prototype#clone()}.
 * Instances are safe to clone and the internal permissions array is defensively
 * copied to avoid accidental mutation sharing between instances.</p>
 *
 * <p>Equality is primarily based on {@code id} when present (> 0),
 * otherwise falls back to {@code email} if available. Note that modifying the
 * {@code id} after insertion in hash-based collections may break collection
 * invariants; prefer immutable keys or avoid using mutable Users as map keys.</p>
 */
public class User implements Prototype<User> {

    private int id;
    private String name;
    private String password;
    private String email;
    private String[] permissions;
    private UserRole role;

    /**
     * Default no-arg constructor for frameworks and for prototype creation.
     * All fields are initialized to default values (id = 0, others null or empty array).
     */
    public User() {
        this.id = 0;
        this.permissions = new String[0];
        this.role = UserRole.GENERIC_USER;
    }

    /**
     * Full constructor that validates and sets fields.
     *
     * @param id       numeric identifier for the user (0 allowed for not-persisted)
     * @param name     user's display name (must not be null or empty)
     * @param password user's password (must not be null or empty)
     * @param email    user's email address (must not be null or empty)
     * @param role     role/actor type (must not be null)
     * @throws IllegalArgumentException if name/password/email are null/empty or id is negative or role is null
     */
    public User(int id, String name, String password, String email, UserRole role) {
        validateId(id);
        validateName(name);
        validatePassword(password);
        validateEmail(email);
        Objects.requireNonNull(role, "User role cannot be null");

        this.id = id;
        this.name = name.trim();
        this.password = password;
        this.email = email.trim();
        this.permissions = new String[0];
        this.role = role;
    }

    /**
     * Copy constructor performing a deep copy of mutable fields.
     *
     * @param other the User instance to copy (must not be null)
     * @throws NullPointerException if other is null
     */
    public User(User other) {
        Objects.requireNonNull(other, "User to copy cannot be null");
        this.id = other.id;
        this.name = other.name;
        this.password = other.password;
        this.email = other.email;
        this.permissions = other.permissions == null ? new String[0] : Arrays.copyOf(other.permissions, other.permissions.length);
        this.role = other.role;
    }

    /**
     * Clone this User (Prototype pattern).
     *
     * @return a new User instance that is a copy of this instance
     */
    @Override
    public User clone() {
        return new User(this);
    }

    // ---------- Getters / Setters ----------

    /**
     * Returns the numeric id of the user.
     *
     * @return user id (0 when not persisted)
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the numeric id for this user.
     *
     * @param id numeric id (must be >= 0)
     * @throws IllegalArgumentException if id is negative
     */
    public void setId(int id) {
        validateId(id);
        this.id = id;
    }

    /**
     * Returns the user's display name.
     *
     * @return name string
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's display name.
     *
     * @param name display name (must not be null or empty)
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        validateName(name);
        this.name = name.trim();
    }

    /**
     * Returns the user's password.
     *
     * <p>Note: this returns the stored password value. In production code you
     * should never store plaintext passwords; use hashed passwords and avoid
     * exposing them. This class follows the project's simplified model.</p>
     *
     * @return password string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * <p>In a real system this method should accept hashed values or trigger
     * hashing. Here it validates non-null/non-empty input consistent with the model.</p>
     *
     * @param password password string (must not be null or empty)
     * @throws IllegalArgumentException if password is null or empty
     */
    public void setPassword(String password) {
        validatePassword(password);
        this.password = password;
    }

    /**
     * Returns the user's email address.
     *
     * @return email string
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email email string (must not be null or empty)
     * @throws IllegalArgumentException if email is null or empty
     */
    public void setEmail(String email) {
        validateEmail(email);
        this.email = email.trim();
    }

    /**
     * Returns a defensive copy of the permissions array.
     *
     * @return copy of permissions (never null)
     */
    public String[] getPermissions() {
        return permissions == null ? new String[0] : Arrays.copyOf(permissions, permissions.length);
    }

    /**
     * Sets the permissions for this user. A defensive copy of the provided array is stored.
     *
     * @param permissions array of permission strings (may be null, treated as empty)
     */
    public void setPermissions(String[] permissions) {
        this.permissions = permissions == null ? new String[0] : Arrays.copyOf(permissions, permissions.length);
    }

    /**
     * Adds a permission to the user's permission set.
     *
     * @param permission permission string to add (must not be null or empty)
     * @throws IllegalArgumentException if permission is null or empty
     */
    public void addPermission(String permission) {
        if (permission == null || permission.trim().isEmpty()) {
            throw new IllegalArgumentException("Permission cannot be null or empty");
        }
        String p = permission.trim();
        String[] current = this.getPermissions();
        String[] next = Arrays.copyOf(current, current.length + 1);
        next[current.length] = p;
        this.permissions = next;
    }

    /**
     * Removes a permission value from this user's permissions.
     *
     * @param permission permission string to remove (may be null)
     * @return true if removed, false otherwise
     */
    public boolean removePermission(String permission) {
        if (permission == null || this.permissions == null || this.permissions.length == 0) return false;
        String p = permission.trim();
        int idx = -1;
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i] != null && permissions[i].equals(p)) {
                idx = i;
                break;
            }
        }
        if (idx < 0) return false;
        String[] next = new String[permissions.length - 1];
        for (int i = 0, j = 0; i < permissions.length; i++) {
            if (i == idx) continue;
            next[j++] = permissions[i];
        }
        this.permissions = next;
        return true;
    }

    /**
     * Returns the role (actor type) of this user.
     *
     * @return user role (never null)
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Sets the role (actor type) of this user.
     *
     * @param role not null
     * @throws NullPointerException if role is null
     */
    public void setRole(UserRole role) {
        this.role = Objects.requireNonNull(role, "Role cannot be null");
    }

    // ---------- Validation helpers ----------

    private static void validateId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("User id cannot be negative");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }

    private static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
    }

    /**
     * Equality is based on {@code id} when present (non-zero), otherwise on {@code email}.
     *
     * @param o other object to compare
     * @return true if equal according to identity rules
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        if (this.id > 0 && user.id > 0) {
            return this.id == user.id;
        }
        return Objects.equals(this.email, user.email);
    }

    /**
     * Hash code consistent with equals.
     *
     * @return hash code integer
     */
    @Override
    public int hashCode() {
        if (id > 0) return Integer.hashCode(id);
        return email == null ? 0 : email.hashCode();
    }

    /**
     * String representation excluding sensitive fields (password omitted).
     *
     * @return short textual representation useful for logs
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", permissions=" + Arrays.toString(permissions) +
                '}';
    }
}
