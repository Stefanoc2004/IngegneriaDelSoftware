package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Content;
import it.unicam.cs.ids.filieraagricola.model.ContentState;
import it.unicam.cs.ids.filieraagricola.service.exception.ContentNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Service class for managing content items in the agricultural platform.
 *
 * <p>This service uses defensive copying: whenever a {@link Content} is stored
 * or returned, a clone of the object is used so that internal state is never
 * exposed to callers. The implementation expects {@link Content#clone()} to
 * return a deep copy (Prototype pattern).</p>
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
     * Stores a content item in the service.
     *
     * <p>The stored instance is a defensive copy (clone) of the provided object.</p>
     *
     * @param content the content to store (must not be null)
     * @return {@code true} if the content was added successfully
     * @throws NullPointerException if {@code content} is null
     */
    public boolean createContent(Content content) {
        Objects.requireNonNull(content, "Content cannot be null");
        return this.contentList.add(copyContent(content));
    }

    /**
     * Approves a content item identified by a numeric identifier.
     *
     * <p>Compatibility helper that converts the numeric id to string and delegates
     * to {@link #approveContentById(String)}.</p>
     *
     * @param numericId the numeric id of the content to approve
     * @return {@code true} if the content was newly approved, {@code false} if it was already approved
     * @throws ContentNotFoundException if no content with the given id exists
     */
    public boolean approveContent(int numericId) throws ContentNotFoundException {
        return approveContentById(String.valueOf(numericId));
    }

    /**
     * Approves a content item identified by its string identifier.
     *
     * <p>If the content is found and not already in {@link ContentState#APPROVED},
     * the stored instance is replaced by a defensively-copied APPROVED instance.</p>
     *
     * @param id the string identifier of the content to approve (must not be null)
     * @return {@code true} if the content transitioned to APPROVED; {@code false} if it was already approved
     * @throws NullPointerException      if {@code id} is null
     * @throws ContentNotFoundException  if no content with the given id exists
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
     * Returns all approved contents suitable for social media.
     *
     * <p>Each returned element is a clone (defensive copy) and the returned list
     * is produced with {@code Stream.toList()} (modern, effectively unmodifiable).</p>
     *
     * @return list of approved content clones (empty list if none)
     */
    public List<Content> showSocialContent() {
        return contentList.stream()
                .filter(Content::isApproved)
                .map(this::copyContent)
                .toList();
    }

    /**
     * Legacy method that returns approved content or {@code null} if none exist.
     *
     * @return list of approved content clones, or {@code null} if none exist
     */
    public List<Content> showSocialContentLegacy() {
        List<Content> approvedContent = showSocialContent();
        return approvedContent.isEmpty() ? null : approvedContent;
    }

    /**
     * Finds a content by its string identifier.
     *
     * @param id the content id to search for (must not be null)
     * @return Optional containing a defensive copy of the found content or empty if not found
     * @throws NullPointerException if {@code id} is null
     */
    public Optional<Content> findContentById(String id) {
        Objects.requireNonNull(id, "Content id cannot be null");
        return contentList.stream()
                .filter(content -> content.getId() != null && content.getId().equals(id))
                .findFirst()
                .map(this::copyContent);
    }

    /**
     * Counts how many contents are in a given {@link ContentState}.
     *
     * @param state the state to count (must not be null)
     * @return the number of contents in the specified state
     * @throws NullPointerException if {@code state} is null
     */
    public long countContentsByState(ContentState state) {
        Objects.requireNonNull(state, "ContentState cannot be null");
        return contentList.stream()
                .filter(content -> content.getState() == state)
                .count();
    }

    /**
     * Returns all pending content items as defensive copies.
     *
     * @return list of pending content clones (empty if none)
     */
    public List<Content> getPendingContent() {
        return contentList.stream()
                .filter(Content::isPending)
                .map(this::copyContent)
                .toList();
    }

    /**
     * Returns all rejected content items as defensive copies.
     *
     * @return list of rejected content clones (empty if none)
     */
    public List<Content> getRejectedContent() {
        return contentList.stream()
                .filter(Content::isRejected)
                .map(this::copyContent)
                .toList();
    }

    /**
     * Returns a defensive copy of the entire internal content list.
     *
     * @return list containing clones of all managed contents
     */
    public List<Content> getContentList() {
        return contentList.stream()
                .map(this::copyContent)
                .toList();
    }

    /**
     * Replaces the internal content storage with a defensive copy of the provided list.
     *
     * @param newContentList new content list (must not be null)
     * @throws IllegalArgumentException if {@code newContentList} is null
     */
    public void setContentList(List<Content> newContentList) {
        if (newContentList == null) throw new IllegalArgumentException("Content list cannot be null");
        this.contentList.clear();
        List<Content> copies = newContentList.stream()
                .map(this::copyContent)
                .toList();
        this.contentList.addAll(copies);
    }

    /* ----------------- private helpers ----------------- */

    /**
     * Returns a defensive copy of the provided content by invoking {@code clone()}.
     *
     * @param content content to copy (may not be null)
     * @return clone of content
     */
    private Content copyContent(Content content) {
        Objects.requireNonNull(content, "Content cannot be null");
        return content.clone();
    }
}
