package it.unicam.cs.ids.filieraagricola.model;

import java.util.Objects;

/**
 * Represents a content item within the agricultural platform.
 * Each content can be related to products, supply chains, or events,
 * and must be validated/approved before publication.
 *
 * <p>Content items go through an approval workflow where they start in
 * PENDING state and can be moved to APPROVED or REJECTED states by
 * authorized users (curators).</p>
 *
 * <p>This class implements the Prototype pattern, allowing for easy
 * creation of new content instances by cloning existing ones.</p>
 */
public class Content implements Prototype {

    /**
     * Unique identifier for the content
     */
    private String id;

    /**
     * Title/name of the content
     */
    private String name;

    /**
     * Detailed description of the content
     */
    private String description;

    /**
     * Current approval state of the content
     */
    private ContentState state;

    /**
     * Default constructor for prototype pattern.
     * Creates a new Content instance with PENDING state.
     * All other fields are initialized to null and should be set
     * using the appropriate setter methods.
     */
    public Content() {
        this.state = ContentState.PENDING;
    }

    /**
     * Constructor with all parameters.
     * Creates a fully initialized Content instance with validation
     * and normalization of input parameters.
     *
     * @param id unique identifier for the content, must not be null or empty
     * @param name title of the content, must not be null or empty
     * @param description detailed description, must not be null or empty
     * @param state current approval state, must not be null
     * @throws IllegalArgumentException if any validation fails
     */
    public Content(String id, String name, String description, ContentState state) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
        validate();
        normalize();
    }

    /**
     * Copy constructor for cloning.
     * Creates a new Content instance with the same properties as the
     * provided content object.
     *
     * @param other the Content instance to copy
     * @throws IllegalArgumentException if other is null
     */
    public Content(Content other) {
        if (other == null) {
            throw new IllegalArgumentException("Content to copy cannot be null");
        }
        this.id = other.id;
        this.name = other.name;
        this.description = other.description;
        this.state = other.state;
    }

    /**
     * Creates a deep copy of this Content instance.
     * This method is part of the Prototype pattern implementation.
     *
     * @return a new Content instance with the same properties as this one
     */
    @Override
    public Content clone() {
        return new Content(this);
    }

    /**
     * Checks if content is approved for publication.
     *
     * @return true if the content state is APPROVED, false otherwise
     */
    public boolean isApproved() {
        return state == ContentState.APPROVED;
    }

    /**
     * Checks if content is pending approval.
     *
     * @return true if the content state is PENDING, false otherwise
     */
    public boolean isPending() {
        return state == ContentState.PENDING;
    }

    /**
     * Checks if content has been rejected.
     *
     * @return true if the content state is REJECTED, false otherwise
     */
    public boolean isRejected() {
        return state == ContentState.REJECTED;
    }

    /**
     * Validates all content fields according to business rules.
     * Called internally during construction and field updates.
     *
     * @throws IllegalArgumentException if any validation fails
     */
    private void validate() {
        if (id != null) validateId(id);
        if (name != null) validateName(name);
        if (description != null) validateDescription(description);
        Objects.requireNonNull(state, "Content state cannot be null");
    }

    /**
     * Normalizes string fields by trimming whitespace.
     * Called internally during construction and field updates.
     */
    private void normalize() {
        if (name != null) {
            name = name.trim();
        }
        if (description != null) {
            description = description.trim();
        }
    }

    /**
     * Validates that the content ID is not empty.
     *
     * @param id the identifier to validate
     * @throws IllegalArgumentException if the ID is empty
     */
    private static void validateId(String id) {
        if (id.trim().isEmpty()) {
            throw new IllegalArgumentException("Content ID cannot be empty");
        }
    }

    /**
     * Validates that the content name is not empty.
     *
     * @param name the name to validate
     * @throws IllegalArgumentException if the name is empty
     */
    private static void validateName(String name) {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Content name cannot be empty");
        }
    }

    /**
     * Validates that the content description is not empty.
     *
     * @param description the description to validate
     * @throws IllegalArgumentException if the description is empty
     */
    private static void validateDescription(String description) {
        if (description.trim().isEmpty()) {
            throw new IllegalArgumentException("Content description cannot be empty");
        }
    }

    /**
     * Returns the unique identifier of this content.
     *
     * @return the content ID, may be null if not set
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this content.
     * The ID is validated to ensure it's not empty if provided.
     *
     * @param id the content identifier to set
     * @throws IllegalArgumentException if id is not null but empty
     */
    public void setId(String id) {
        this.id = id;
        if (id != null) validateId(id);
    }

    /**
     * Returns the name/title of this content.
     *
     * @return the content name, may be null if not set
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name/title for this content.
     * The name is validated and normalized (trimmed) if provided.
     *
     * @param name the content name to set
     * @throws IllegalArgumentException if name is not null but empty after trimming
     */
    public void setName(String name) {
        this.name = name;
        if (name != null) {
            validateName(name);
            this.name = name.trim();
        }
    }

    /**
     * Returns the detailed description of this content.
     *
     * @return the content description, may be null if not set
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the detailed description for this content.
     * The description is validated and normalized (trimmed) if provided.
     *
     * @param description the content description to set
     * @throws IllegalArgumentException if description is not null but empty after trimming
     */
    public void setDescription(String description) {
        this.description = description;
        if (description != null) {
            validateDescription(description);
            this.description = description.trim();
        }
    }

    /**
     * Returns the current approval state of this content.
     *
     * @return the content state, never null
     */
    public ContentState getState() {
        return state;
    }

    /**
     * Sets the approval state for this content.
     *
     * @param state the new content state
     * @throws IllegalArgumentException if state is null
     */
    public void setState(ContentState state) {
        Objects.requireNonNull(state, "Content state cannot be null");
        this.state = state;
    }

    /**
     * Indicates whether some other object is "equal to" this Content.
     * Two Content objects are considered equal if they have the same ID.
     *
     * @param o the reference object with which to compare
     * @return true if this object is equal to the o argument; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(id, content.id);
    }

    /**
     * Returns a hash code value for this Content.
     * The hash code is based on the content ID.
     *
     * @return a hash code value for this object
     */
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a string representation of this Content.
     * The string includes all major fields for debugging purposes.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Content{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                '}';
    }
}