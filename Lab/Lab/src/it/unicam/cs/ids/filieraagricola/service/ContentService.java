package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Content;
import it.unicam.cs.ids.filieraagricola.model.ContentState;
import it.unicam.cs.ids.filieraagricola.service.exception.NotFoundException;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Service class for managing content items in the agricultural platform.
 *
 * <p>This service handles the creation, retrieval, and approval workflow of content items.
 * It ensures data integrity through validation and manages state transitions.
 * Content objects are treated as immutable data transfer objects once created,
 * with updates resulting in new instances or modifications handled internally by the service.</p>
 */
public class ContentService {

    private final List<Content> contentList;

    /**
     * Constructs an empty ContentService.
     */
    public ContentService() {
        this.contentList = new ArrayList<>();
    }

    /**
     * Creates and stores a new content item.
     *
     * @param name The title/name of the content.
     * @param description The detailed description of the content.
     * @return The newly created Content instance.
     * @throws ValidationException If the name or description is null or empty.
     */
    public Content createContent(String name, String description) {
        validateContentData(name, description);
        Content newContent = new Content(name.trim(), description.trim(), ContentState.PENDING);
        this.contentList.add(newContent);
        return newContent;
    }

    /**
     * Approves a content item identified by its string identifier.
     *
     * <p>If the content is found and not already in {@link ContentState#APPROVED},
     * its state is updated to APPROVED.</p>
     *
     * @param id The string identifier of the content to approve.
     * @return True if the content transitioned to APPROVED; false if it was already approved.
     * @throws ValidationException If {@code id} is null or empty.
     * @throws NotFoundException If no content with the given id exists.
     */
    public boolean approveContentById(String id) {
        validateId(id);

        Optional<Integer> indexOpt = findContentIndex(id);

        if (indexOpt.isEmpty()) {
            throw new NotFoundException("Content with ID " + id + " not found.");
        }

        int index = indexOpt.get();
        Content content = contentList.get(index);

        if (content.getState() == ContentState.APPROVED) {
            return false; // Already approved
        }

        // Create a new Content instance with the APPROVED state
        Content approvedContent = new Content(
                content.getId(),
                content.getName(),
                content.getDescription(),
                ContentState.APPROVED
        );

        contentList.set(index, approvedContent);
        return true;
    }

    /**
     * Rejects a content item identified by its string identifier.
     *
     * <p>If the content is found and not already in {@link ContentState#REJECTED},
     * its state is updated to REJECTED.</p>
     *
     * @param id The string identifier of the content to reject.
     * @return True if the content transitioned to REJECTED; false if it was already rejected.
     * @throws ValidationException If {@code id} is null or empty.
     * @throws NotFoundException If no content with the given id exists.
     */
    public boolean rejectContentById(String id) {
        validateId(id);

        Optional<Integer> indexOpt = findContentIndex(id);

        if (indexOpt.isEmpty()) {
            throw new NotFoundException("Content with ID " + id + " not found.");
        }

        int index = indexOpt.get();
        Content content = contentList.get(index);

        if (content.getState() == ContentState.REJECTED) {
            return false; // Already rejected
        }

        // Create a new Content instance with the REJECTED state
        Content rejectedContent = new Content(
                content.getId(),
                content.getName(),
                content.getDescription(),
                ContentState.REJECTED
        );

        contentList.set(index, rejectedContent);
        return true;
    }

    /**
     * Returns all approved contents suitable for social media.
     *
     * @return An unmodifiable list of approved content items (empty list if none).
     */
    public List<Content> showSocialContent() {
        return contentList.stream()
                .filter(content -> content.getState() == ContentState.APPROVED)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll); // Collect to a new mutable list
    }

    /**
     * Finds a content by its string identifier.
     *
     * @param id The content id to search for.
     * @return An Optional containing the found content or empty if not found.
     * @throws ValidationException If {@code id} is null or empty.
     */
    public Optional<Content> findContentById(String id) {
        validateId(id);
        return contentList.stream()
                .filter(content -> content.getId().equals(id))
                .findFirst();
    }

    /**
     * Counts how many contents are in a given {@link ContentState}.
     *
     * @param state The state to count.
     * @return The number of contents in the specified state.
     * @throws ValidationException If {@code state} is null.
     */
    public long countContentsByState(ContentState state) {
        Objects.requireNonNull(state, "ContentState cannot be null");
        return contentList.stream()
                .filter(content -> content.getState() == state)
                .count();
    }

    /**
     * Returns all pending content items.
     *
     * @return An unmodifiable list of pending content items (empty if none).
     */
    public List<Content> getPendingContent() {
        return contentList.stream()
                .filter(content -> content.getState() == ContentState.PENDING)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Returns all rejected content items.
     *
     * @return An unmodifiable list of rejected content items (empty if none).
     */
    public List<Content> getRejectedContent() {
        return contentList.stream()
                .filter(content -> content.getState() == ContentState.REJECTED)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Returns an unmodifiable list of all internal content items.
     *
     * @return An unmodifiable list containing all managed contents.
     */
    public List<Content> getAllContents() {
        return Collections.unmodifiableList(new ArrayList<>(contentList));
    }

    /**
     * Validates the name and description of a content item.
     *
     * @param name The name of the content.
     * @param description The description of the content.
     * @throws ValidationException If the name or description is null or empty.
     */
    private void validateContentData(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Content name cannot be null or empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException("Content description cannot be null or empty.");
        }
    }

    /**
     * Validates that the content ID is not null or empty.
     *
     * @param id The identifier to validate.
     * @throws ValidationException If the ID is null or empty.
     */
    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("Content ID cannot be null or empty.");
        }
    }

    /**
     * Finds the index of a content item in the internal list by its ID.
     *
     * @param id The ID of the content to find.
     * @return An Optional containing the index of the content, or empty if not found.
     */
    private Optional<Integer> findContentIndex(String id) {
        return IntStream.range(0, contentList.size())
                .filter(i -> contentList.get(i).getId().equals(id))
                .boxed()
                .findFirst();
    }
}
