package it.unicam.cs.ids.filieraagricola.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a content item within the agricultural platform.
 * Each content can be related to products, supply chains, or events.
 * It holds data about the content's unique identifier, name, description, and current approval state.
 *
 * <p>This class is a pure data model and does not contain business logic for validation or state transitions.
 * Business logic, such as content approval workflow, is handled by {@link it.unicam.cs.ids.filieraagricola.service.ContentService}.</p>
 */
public class Content {

    /**
     * Unique identifier for the content.
     */
    private final String id;

    /**
     * Title/name of the content.
     */
    private final String name;

    /**
     * Detailed description of the content.
     */
    private final String description;

    /**
     * Current approval state of the content.
     */
    private final ContentState state;

    /**
     * Constructs a new Content instance with a generated ID and PENDING state.
     * This constructor is primarily for internal use by services when creating new content.
     *
     * @param name The title/name of the content.
     * @param description The detailed description of the content.
     * @param state The initial approval state of the content.
     */
    public Content(String name, String description, ContentState state) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.state = state;
    }

    /**
     * Constructs a new Content instance with a specified ID.
     * This constructor is typically used when reconstructing a Content object from persistence
     * or when a specific ID is required.
     *
     * @param id The unique identifier for the content.
     * @param name The title/name of the content.
     * @param description The detailed description of the content.
     * @param state The current approval state of the content.
     */
    public Content(String id, String name, String description, ContentState state) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
    }

    /**
     * Returns the unique identifier of this content.
     *
     * @return The content ID, never null after construction.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name/title of this content.
     *
     * @return The content name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the detailed description of this content.
     *
     * @return The content description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the current approval state of this content.
     *
     * @return The content state, never null.
     */
    public ContentState getState() {
        return state;
    }

    /**
     * Indicates whether some other object is "equal to" this Content.
     * Two Content objects are considered equal if they have the same ID.
     *
     * @param o The reference object with which to compare.
     * @return True if this object is equal to the o argument; false otherwise.
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
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a string representation of this Content.
     * The string includes all major fields for debugging purposes.
     *
     * @return A string representation of this object.
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
