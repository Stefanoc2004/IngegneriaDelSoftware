package it.unicam.cs.ids.filieraagricola.model;


import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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
@Entity
public class User implements Prototype<User> {
    @Id
    private String id;
    private String name;
    private String password;
    private String email;
    @Convert(converter = PermissionsArrayConverter.class)
    @Column(name = "permissions",nullable = false)
    private UserRole[] permissions;
    /**
     * Default no-arg constructor for frameworks and for prototype creation.
     * All fields are initialized to default values (id = 0, others null or empty array).
     */
    public User() {
        this.id = "";
        this.permissions = new UserRole[0];
    }

    /**
     * Full constructor that validates and sets fields.
     *
     * @param id       numeric identifier for the user (0 allowed for not-persisted)
     * @param name     user's display name (must not be null or empty)
     * @param password user's password (must not be null or empty)
     * @param email    user's email address (must not be null or empty)
     * @throws IllegalArgumentException if name/password/email are null/empty or id is negative or role is null
     */
    public User(String id, String name, String password, String email) {
        validateId(id);
        validateName(name);
        validatePassword(password);
        validateEmail(email);

        this.id = id;
        this.name = name.trim();
        this.password = password;
        this.email = email.trim();
        this.permissions = new UserRole[0];
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
        this.permissions = other.permissions == null ? new UserRole[0] : Arrays.copyOf(other.permissions, other.permissions.length);
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
     * Returns the identifier of the user as a string.
     *
     * @return user id (0 when not persisted)
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the identifier for this user.
     *
     * @param id identifier string (must be non-null and non-empty)
     * @throws IllegalArgumentException if id is null or empty
     */
    public void setId(String id) {
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
    public UserRole[] getPermissions() {
        return permissions == null ? new UserRole[0] : Arrays.copyOf(permissions, permissions.length);
    }

    /**
     * Sets the permissions for this user. A defensive copy of the provided array is stored.
     *
     * @param permissions array of permission strings (may be null, treated as empty)
     */
    public void setPermissions(UserRole... permissions) {
        this.permissions = permissions == null ? new UserRole[0] : Arrays.copyOf(permissions, permissions.length);
    }

    /**
     * Adds a permission to the user's permission set.
     *
     * @param permission permission string to add (must not be null or empty)
     * @throws IllegalArgumentException if permission is null or empty
     */
    public void addPermission(UserRole permission) {
        if (permission == null ) {
            throw new IllegalArgumentException("Permission cannot be null or empty");
        }
        UserRole[] current = this.getPermissions();
        UserRole[] next = Arrays.copyOf(current, current.length + 1);
        next[current.length] = permission;
        this.permissions = next;
    }

    /**
     * Removes a permission value from this user's permissions.
     *
     * @param permission permission string to remove (may be null)
     * @return true if removed, false otherwise
     */
    public boolean removePermission(UserRole permission) {
        if (permission == null || this.permissions == null || this.permissions.length == 0) return false;
        int idx = -1;
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i] != null && permissions[i].equals(permission)) {
                idx = i;
                break;
            }
        }
        if (idx < 0) return false;
        UserRole[] next = new UserRole[permissions.length - 1];
        for (int i = 0, j = 0; i < permissions.length; i++) {
            if (i == idx) continue;
            next[j++] = permissions[i];
        }
        this.permissions = next;
        return true;
    }



    // ---------- Validation helpers ----------

    /**
     * Validates the identifier according to the current implementation rules.
     *
     * <p><b>Current behaviour:</b> throws an {@link IllegalArgumentException}
     * when the provided id is not {@code null}. This reflects the existing
     * implementation and is intentionally documented without altering logic.</p>
     *
     * @param id candidate identifier
     * @throws IllegalArgumentException when {@code id} is not {@code null}
     */
    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("User id cannot be null or empty");
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

    public void giveNewId() {
        this.id = UUID.randomUUID().toString();
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
        if (this.id != null && user.id != null) {
            return this.id.equals(user.id);
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
        if (id != null) return id.hashCode();
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
                ", permissions=" + Arrays.toString(permissions) +
                '}';
    }
}
