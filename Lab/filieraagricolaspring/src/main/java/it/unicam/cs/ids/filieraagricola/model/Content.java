package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.Objects;
import java.util.UUID; // Added import for UUID

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
@Entity
public class Content  {
    /**
     * Unique identifier for the content. Automatically generated if not provided.
     */
    @Id
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


    private ContentType type;

    @ManyToOne
    private SupplyChainPoint point;

    /**
     * Default constructor for prototype pattern.
     * Creates a new Content instance with a generated ID and PENDING state.
     * All other fields are initialized to null and should be set
     * using the appropriate setter methods.
     */
    public Content() {
        this.id = UUID.randomUUID().toString(); // Automatically generate ID
        this.state = ContentState.PENDING;
    }

    /**
     * Constructor with all parameters.
     * Creates a fully initialized Content instance with validation
     * and normalization of input parameters. If the ID is null or empty, a new one is generated.
     *
     * @param id unique identifier for the content, if null or empty, a new UUID will be generated
     * @param name title of the content, must not be null or empty
     * @param description detailed description, must not be null or empty
     * @param state current approval state, must not be null
     * @throws IllegalArgumentException if any validation fails
     */
    public Content(String id, String name, String description, ContentState state) {
        this.id = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id.trim(); // Ensure ID is always set and trimmed
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
        // ID is now always generated or provided, so it should not be null or empty here.
        // If it was possible for ID to be null/empty after construction, validateId(id) would be needed.
        // For now, we assume it's valid due to constructor logic.
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
        if (id == null || id.trim().isEmpty()) { // Added null check for robustness
            throw new IllegalArgumentException("Content ID cannot be null or empty");
        }
    }

    /**
     * Validates that the content name is not empty.
     *
     * @param name the name to validate
     * @throws IllegalArgumentException if the name is empty
     */
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) { // Added null check for robustness
            throw new IllegalArgumentException("Content name cannot be null or empty");
        }
    }

    /**
     * Validates that the content description is not empty.
     *
     * @param description the description to validate
     * @throws IllegalArgumentException if the description is empty
     */
    private static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) { // Added null check for robustness
            throw new IllegalArgumentException("Content description cannot be null or empty");
        }
    }

    /**
     * Returns the unique identifier of this content.
     *
     * @return the content ID, never null after construction
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this content.
     * The ID is validated to ensure it's not null or empty if provided.
     *
     * @param id the content identifier to set
     * @throws IllegalArgumentException if id is null or empty after trimming
     */
    public void setId(String id) {
        validateId(id); // Validate even if null is passed, to throw exception
        this.id = id.trim();
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
     * @throws IllegalArgumentException if name is null or empty after trimming
     */
    public void setName(String name) {
        validateName(name); // Validate even if null is passed, to throw exception
        this.name = name.trim();
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
     * @throws IllegalArgumentException if description is null or empty after trimming
     */
    public void setDescription(String description) {
        validateDescription(description); // Validate even if null is passed, to throw exception
        this.description = description.trim();
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

    public ContentType getType() {
        return type;
    }

    /**
     * Sets the semantic type of this content.
     *
     * <p>The type categorizes the content (for example certificates or procedures)
     * and can be used by services and UI to filter or render appropriate views.</p>
     *
     * @param type the {@link ContentType} to assign to this content (may be {@code null})
     */
    public void setType(ContentType type) {
        this.type = type;
    }

    /**
     * Returns a hash code value for this Content.
     * The hash code is based on the content ID.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    /**
     * Returns the supply-chain point associated with this content.
     *
     * <p>When set, this link identifies the specific {@link SupplyChainPoint}
     * (e.g., farm, market) the content refers to.</p>
     *
     * @return the linked {@link SupplyChainPoint}, or {@code null} if none is set
     */
    public SupplyChainPoint getPoint() {
        return point;
    }

    /**
     * Associates this content with a supply-chain point.
     *
     * @param point the {@link SupplyChainPoint} this content refers to (may be {@code null})
     */
    public void setPoint(SupplyChainPoint point) {
        this.point = point;
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