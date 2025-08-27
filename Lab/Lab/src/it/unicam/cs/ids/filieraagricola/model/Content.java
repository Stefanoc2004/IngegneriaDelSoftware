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

    public static Content create(String name, String description) {}

    public boolean isApproved() {
        return false;
    }

    public boolean isPending() {
        return false;
    }

    public boolean isRejected() {
        return false;
    }

    private static void validateId(String id){
        if (id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Content ID cannot be null or empty");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Content name cannot be null or empty");
        }
    }

    private static void validateDescription(String description){
        if (description == null || description.trim().isEmpty()){
            throw new IllegalArgumentException("Content description cannot be null or empty");
        }
    }

    private static String generateId(String base) {
        return (base + "_" + System.currentTimeMillis())
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }

}