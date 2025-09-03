package it.unicam.cs.ids.filieraagricola.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Abstract base class representing an actor in the agricultural supply chain platform.
 *
 * <p>An actor is any entity that participates in the agricultural supply chain,
 * such as producers, transformers, distributors, curators, etc. Each actor has
 * a unique identifier, basic contact information, and a specific type that
 * determines their role and capabilities within the platform.</p>
 *
 * <p>This class implements the Prototype pattern to enable defensive copying
 * and safe object management throughout the system.</p>
 */
public abstract class Actor implements Prototype<Actor> {

    /**
     * Unique identifier for the actor. Automatically generated if not provided.
     */
    protected String id;

    /**
     * Display name of the actor (person or organization)
     */
    protected String name;

    /**
     * Email address for contact purposes
     */
    protected String email;

    /**
     * Phone number for contact purposes
     */
    protected String phone;

    /**
     * Physical address or location
     */
    protected String address;

    /**
     * Type of actor (defines role and permissions)
     */
    protected ActorType type;

    /**
     * Whether the actor is currently active in the platform
     */
    protected boolean active;

    /**
     * Default constructor for frameworks and prototype pattern.
     * Generates a unique ID and sets the actor as active by default.
     */
    protected Actor() {
        this.id = UUID.randomUUID().toString();
        this.active = true;
    }

    /**
     * Full constructor with validation and normalization.
     * If the ID is null or empty, a new one is generated.
     *
     * @param id unique identifier (if null or empty, a new UUID will be generated)
     * @param name display name (must not be null or empty)
     * @param email email address (must not be null or empty)
     * @param phone phone number (may be null)
     * @param address physical address (may be null)
     * @param type actor type (must not be null)
     * @param active whether the actor is active
     * @throws IllegalArgumentException if any validation fails
     */
    protected Actor(String id, String name, String email, String phone, String address, ActorType type, boolean active) {
        this.id = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id.trim();
        validateName(name);
        validateEmail(email);
        Objects.requireNonNull(type, "Actor type cannot be null");

        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.phone = phone != null ? phone.trim() : null;
        this.address = address != null ? address.trim() : null;
        this.type = type;
        this.active = active;
    }

    /**
     * Copy constructor for cloning.
     *
     * @param other the Actor instance to copy
     * @throws IllegalArgumentException if other is null
     */
    protected Actor(Actor other) {
        Objects.requireNonNull(other, "Actor to copy cannot be null");
        this.id = other.id;
        this.name = other.name;
        this.email = other.email;
        this.phone = other.phone;
        this.address = other.address;
        this.type = other.type;
        this.active = other.active;
    }

    /**
     * Returns the unique identifier of this actor.
     *
     * @return the actor ID, never null after construction
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this actor.
     *
     * @param id the actor identifier to set
     * @throws IllegalArgumentException if id is null or empty after trimming
     */
    public void setId(String id) {
        validateId(id);
        this.id = id.trim();
    }

    /**
     * Returns the display name of this actor.
     *
     * @return the actor name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the display name for this actor.
     *
     * @param name the actor name to set
     * @throws IllegalArgumentException if name is null or empty after trimming
     */
    public void setName(String name) {
        validateName(name);
        this.name = name.trim();
    }

    /**
     * Returns the email address of this actor.
     *
     * @return the actor email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address for this actor.
     *
     * @param email the actor email to set
     * @throws IllegalArgumentException if email is null or empty after trimming
     */
    public void setEmail(String email) {
        validateEmail(email);
        this.email = email.trim().toLowerCase();
    }

    /**
     * Returns the phone number of this actor.
     *
     * @return the actor phone number, may be null
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number for this actor.
     *
     * @param phone the actor phone number to set, may be null
     */
    public void setPhone(String phone) {
        this.phone = phone != null ? phone.trim() : null;
    }

    /**
     * Returns the address of this actor.
     *
     * @return the actor address, may be null
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address for this actor.
     *
     * @param address the actor address to set, may be null
     */
    public void setAddress(String address) {
        this.address = address != null ? address.trim() : null;
    }

    /**
     * Returns the type of this actor.
     *
     * @return the actor type, never null
     */
    public ActorType getType() {
        return type;
    }

    /**
     * Sets the type for this actor.
     *
     * @param type the actor type to set
     * @throws IllegalArgumentException if type is null
     */
    public void setType(ActorType type) {
        Objects.requireNonNull(type, "Actor type cannot be null");
        this.type = type;
    }

    /**
     * Returns whether this actor is currently active.
     *
     * @return true if the actor is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status for this actor.
     *
     * @param active the active status to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Checks if this actor can upload content to the platform.
     * By default, only certain actor types can upload content.
     *
     * @return true if the actor can upload content
     */
    public boolean canUploadContent() {
        return active && (type == ActorType.PRODUCER ||
                type == ActorType.TRANSFORMER ||
                type == ActorType.DISTRIBUTOR);
    }

    /**
     * Checks if this actor can approve content on the platform.
     *
     * @return true if the actor can approve content
     */
    public boolean canApproveContent() {
        return active && type == ActorType.CURATOR;
    }

    /**
     * Checks if this actor can organize events on the platform.
     *
     * @return true if the actor can organize events
     */
    public boolean canOrganizeEvents() {
        return active && type == ActorType.ANIMATOR;
    }

    /**
     * Checks if this actor can make purchases on the platform.
     *
     * @return true if the actor can make purchases
     */
    public boolean canMakePurchases() {
        return active && (type == ActorType.BUYER || type == ActorType.DISTRIBUTOR);
    }

    // ----------------- validation helpers -----------------

    /**
     * Validates that the actor ID is not null or empty.
     *
     * @param id the identifier to validate
     * @throws IllegalArgumentException if the ID is null or empty
     */
    private static void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Actor ID cannot be null or empty");
        }
    }

    /**
     * Validates that the actor name is not null or empty.
     *
     * @param name the name to validate
     * @throws IllegalArgumentException if the name is null or empty
     */
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Actor name cannot be null or empty");
        }
    }

    /**
     * Validates that the actor email is not null or empty and has basic email format.
     *
     * @param email the email to validate
     * @throws IllegalArgumentException if the email is null, empty, or invalid format
     */
    private static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Actor email cannot be null or empty");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Actor email must have valid format");
        }
    }

    // ----------------- equals/hashCode/toString -----------------

    /**
     * Indicates whether some other object is "equal to" this Actor.
     * Two Actor objects are considered equal if they have the same ID.
     *
     * @param o the reference object with which to compare
     * @return true if this object is equal to the o argument; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(id, actor.id);
    }

    /**
     * Returns a hash code value for this Actor.
     * The hash code is based on the actor ID.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a string representation of this Actor.
     * The string includes all major fields for debugging purposes.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type +
                ", active=" + active +
                '}';
    }
}