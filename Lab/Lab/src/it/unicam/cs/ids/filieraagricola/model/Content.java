package it.unicam.cs.ids.filieraagricola.model;

import java.util.Objects;

public class Content implements Prototype<Content> {

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

    public Content(String id, String name, String description, ContentState state) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
        validate();
        normalize();
    }

    public Content(Content other) {
        if (other == null) {
            throw new IllegalArgumentException("Content to copy cannot be null");
        }
        this.id = other.id;
        this.name = other.name;
        this.description = other.description;
        this.state = other.state;
    }

    @Override
    public Content clone() {
        return new Content(this);
    }

    public boolean isApproved() {
        return state == ContentState.APPROVED;
    }

    public boolean isPending() {
        return state == ContentState.PENDING;
    }

    public boolean isRejected() {
        return state == ContentState.REJECTED;
    }

    private void validate() {
        if (id != null) validateId(id);
        if (name != null) validateName(name);
        if (description != null) validateDescription(description);
        Objects.requireNonNull(state, "Content state cannot be null");
    }

    private void normalize() {
        if (name != null) {
            name = name.trim();
        }
        if (description != null) {
            description = description.trim();
        }
    }

    private static void validateId(String id) {
        if (id.trim().isEmpty()) {
            throw new IllegalArgumentException("Content ID cannot be empty");
        }
    }

    private static void validateName(String name) {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Content name cannot be empty");
        }
    }

    private static void validateDescription(String description) {
        if (description.trim().isEmpty()) {
            throw new IllegalArgumentException("Content description cannot be empty");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        if (id != null) validateId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (name != null) {
            validateName(name);
            this.name = name.trim();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if (description != null) {
            validateDescription(description);
            this.description = description.trim();
        }
    }

    public ContentState getState() {
        return state;
    }

    public void setState(ContentState state) {
        Objects.requireNonNull(state, "Content state cannot be null");
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(id, content.id);
    }

    public int hashCode() {
        return Objects.hash(id);
    }

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