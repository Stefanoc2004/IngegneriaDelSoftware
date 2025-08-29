package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Content;
import it.unicam.cs.ids.filieraagricola.model.ContentState;
import it.unicam.cs.ids.filieraagricola.model.Prototype;
import it.unicam.cs.ids.filieraagricola.service.exception.ContentNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Service class for managing agricultural platform content.
 *
 * <p>The service applies defensive-copying when possible: if a Content instance
 * implements {@link Prototype} it will be cloned prior to being stored or returned.
 * If Content is immutable the same instance may be stored/returned.</p>
 */
public class ContentService {

    private final List<Content> contentList;

    /**
     * Constructs a new ContentService with an empty content list.
     */
    public ContentService() {
        this.contentList = new ArrayList<>();
    }

    /**
     * Creates and stores a new content item in the service.
     * If the provided content implements {@link Prototype} a clone is stored.
     *
     * @param content the content item to be added (must not be null)
     * @return true if the content was successfully added
     * @throws IllegalArgumentException if content is null
     */
    public boolean createContent(Content content) {
        Objects.requireNonNull(content, "Content cannot be null");
        return this.contentList.add(copyContent(content));
    }

    /**
     * Approves a content item identified by its numeric ID.
     * Backwards-compatible helper that converts numeric id to string.
     *
     * @param numericId numeric identifier of content to approve
     * @return true if the content was newly approved, false if already approved
     * @throws ContentNotFoundException if no content exists with the specified ID
     */
    public boolean approveContent(int numericId) throws ContentNotFoundException {
        return approveContentById(String.valueOf(numericId));
    }

    /**
     * Approves a content item identified by its string ID.
     * If the content is found and not already approved, replaces the stored
     * instance with an APPROVED instance (stored defensively).
     *
     * @param id string identifier of the content to approve (must not be null)
     * @return true if the content was newly approved, false if already approved
     * @throws ContentNotFoundException if no content exists with the specified ID
     * @throws IllegalArgumentException if id is null
     */
    public boolean approveContentById(String id) throws ContentNotFoundException {
        Objects.requireNonNull(id, "Content id cannot be null");

        Optional<Integer> indexOpt = IntStream.range(0, contentList.size())
                .filter(i -> {
                    Content c = contentList.get(i);
                    return c.getId() != null && c.getId().equals(id);
                })
                .boxed()
                .findFirst();

        if (indexOpt.isEmpty()) {
            throw new ContentNotFoundException();
        }

        int index = indexOpt.get();
        Content content = contentList.get(index);

        if (content.isApproved()) {
            return false;
        }

        Content approvedContent = new Content(
                content.getId(),
                content.getName(),
                content.getDescription(),
                ContentState.APPROVED
        );

        contentList.set(index, copyContent(approvedContent));
        return true;
    }

    /**
     * Retrieves all approved content suitable for social media publishing.
     * Returned list contains defensive copies when possible.
     *
     * @return unmodifiable list containing copies of approved content items
     */
    public List<Content> showSocialContent() {
        List<Content> result = this.contentList.stream()
                .filter(Content::isApproved)
                .map(this::copyContent)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    /**
     * Legacy version of social content retrieval that returns null if none exist.
     *
     * @return list of approved content items or null if none exist
     */
    public List<Content> showSocialContentLegacy() {
        List<Content> approvedContent = showSocialContent();
        return approvedContent.isEmpty() ? null : approvedContent;
    }

    /**
     * Finds a content item by its unique identifier.
     *
     * @param id content id to search for (must not be null)
     * @return Optional containing a defensive copy of the content if found
     * @throws IllegalArgumentException if id is null
     */
    public Optional<Content> findContentById(String id) {
        Objects.requireNonNull(id, "Content id cannot be null");
        return contentList.stream()
                .filter(content -> content.getId() != null && content.getId().equals(id))
                .findFirst()
                .map(this::copyContent);
    }

    /**
     * Counts the number of content items in a specific approval state.
     *
     * @param state content state to count (must not be null)
     * @return number of content items in the specified state
     * @throws IllegalArgumentException if state is null
     */
    public long countContentsByState(ContentState state) {
        Objects.requireNonNull(state, "ContentState cannot be null");
        return contentList.stream()
                .filter(content -> content.getState() == state)
                .count();
    }

    /**
     * Retrieves all content items that are pending approval.
     *
     * @return unmodifiable list of pending content items (defensive copies when applicable)
     */
    public List<Content> getPendingContent() {
        List<Content> result = contentList.stream()
                .filter(Content::isPending)
                .map(this::copyContent)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    /**
     * Retrieves all content items that have been rejected.
     *
     * @return unmodifiable list of rejected content items (defensive copies when applicable)
     */
    public List<Content> getRejectedContent() {
        List<Content> result = contentList.stream()
                .filter(Content::isRejected)
                .map(this::copyContent)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns a defensive copy of the internal content list.
     *
     * @return new list containing defensive copies of all managed content items
     */
    public List<Content> getContentList() {
        return Collections.unmodifiableList(
                contentList.stream()
                        .map(this::copyContent)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Replaces the internal content list with a defensive copy of the provided list.
     * Each element is cloned if it implements {@link Prototype}.
     *
     * @param newContentList new content list (must not be null)
     * @throws IllegalArgumentException if newContentList is null
     */
    public void setContentList(List<Content> newContentList) {
        if (newContentList == null) throw new IllegalArgumentException("Content list cannot be null");
        this.contentList.clear();
        this.contentList.addAll(newContentList.stream()
                .map(this::copyContent)
                .collect(Collectors.toList()));
    }

    // ----------------- private helpers -----------------

    /**
     * Returns a cloned Content if it implements {@link Prototype}; otherwise
     * returns the original instance (assumed immutable).
     *
     * @param content content to copy
     * @return cloned or original content
     */
    private Content copyContent(Content content) {
        if (content instanceof Prototype) {
            return (Content) ((Prototype) content).clone();
        }
        return content;
    }
}