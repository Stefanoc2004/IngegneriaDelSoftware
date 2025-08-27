package it.unicam.cs.ids.filieraagricola.model;

import java.util.Objects;

/**
 * Represents a content item within the agricultural platform.
 * Each content can be related to products, supply chains, or events,
 * and must be validated/approved before publication.
 *
 * @param id unique identifier for the content
 * @param name title of the content
 * @param description detailed description
 * @param state current approval state of the content
 */
public record Content(
        String id,
        String name,
        String description,
        ContentState state
) {

    /**
     * Compact constructor for validation and normalization.
     */
    public Content {
        validateId(id);
        validateName(name);
        validateDescription(description);
        Objects.requireNonNull(state, "Content state cannot be null");

        name = name.trim();
        description = description.trim();
    }

    /**
     * Factory method to create a new Content in PENDING state.
     *
     * @param name content title
     * @param description content description
     * @return a new validated Content instance
     */
    public static Content create(String name, String description) {
        String id = generateId(name);
        return new Content(id, name, description, ContentState.PENDING);
    }

    /**
     * Checks if content is approved.
     * @return true if state == APPROVED
     */
    public boolean isApproved() {
        return state == ContentState.APPROVED;
    }

    /**
     * Checks if content is pending approval.
     * @return true if state == PENDING
     */
    public boolean isPending() {
        return state == ContentState.PENDING;
    }

    /**
     * Checks if content is rejected.
     * @return true if state == REJECTED
     */
    public boolean isRejected() {
        return state == ContentState.REJECTED;
    }


    /**
     * Validates that the content ID is not null or empty.
     *
     * @param id the identifier to validate
     * @throws IllegalArgumentException if the ID is null or empty
     */
    private static void validateId(String id){
        if (id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Content ID cannot be null or empty");
        }
    }

    /**
     * Validates that the content name is not null or empty
     * @param name the name to validate
     * @throws IllegalArgumentException if the name is null or empty
     */
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Content name cannot be null or empty");
        }
    }

    /**
     * Validates that the content description is not null or empty.
     *
     * @param description the description to validate
     * @throws IllegalArgumentException if the description is null or empty
     */
    private static void validateDescription(String description){
        if (description == null || description.trim().isEmpty()){
            throw new IllegalArgumentException("Content description cannot be null or empty");
        }
    }


    /**
     * Generates a unique identifier for the content based on its name and the current timestamp.
     *
     * @param base the base string used for generating the identifier
     * @return a normalized unique identifier
     */
    private static String generateId(String base) {
        return (base + "_" + System.currentTimeMillis())
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }
}