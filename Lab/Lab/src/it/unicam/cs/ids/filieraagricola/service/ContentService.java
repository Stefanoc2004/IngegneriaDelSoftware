package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Content;
import it.unicam.cs.ids.filieraagricola.model.ContentState;
import it.unicam.cs.ids.filieraagricola.service.exception.ContentNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Service class for managing agricultural platform content.
 * Provides operations for creating, approving, and retrieving content items.
 */
public class ContentService {

    /**
     * Internal storage for all content items managed by this service.
     * Uses ArrayList for efficient indexed access and modification operations.
     */
    private List<Content> contentList;

    /**
     * Constructs a new ContentService with an empty content list.
     * Initializes the internal storage for managing content items.
     */
    public ContentService() {
        this.contentList = new ArrayList<>();
    }

    /**
     * Creates and stores a new content item in the service.
     *
     * @param content the content item to be added, must not be null
     * @return true if the content was successfully added to the service
     * @throws NullPointerException if content is null
     */
    public boolean createContent(Content content) {
        return this.contentList.add(content);
    }

    /**
     * Approves a content item identified by its numeric ID.
     * This method maintains backward compatibility by accepting integer IDs
     * and internally converting them to string format for processing.
     *
     * @param numericId the numeric identifier of the content to approve
     * @return true if the content was successfully approved, false if already approved
     * @throws ContentNotFoundException if no content exists with the specified ID
     */
    public boolean approveContent(int numericId) throws ContentNotFoundException {
        String targetId = String.valueOf(numericId);
        return approveContentById(targetId);
    }

    /**
     * Approves a content item identified by its string ID.
     * This method provides direct string ID handling for optimal performance
     * when working with native Content record identifiers.
     *
     * @param id the string identifier of the content to approve, must not be null
     * @return true if the content was newly approved, false if already approved
     * @throws ContentNotFoundException if no content exists with the specified ID
     * @throws NullPointerException if id is null
     */
    public boolean approveContentById(String id) throws ContentNotFoundException {
        // Trova l'indice del contenuto usando stream
        Optional<Integer> indexOpt = IntStream.range(0, contentList.size())
                .filter(i -> contentList.get(i).id().equals(id))
                .boxed()
                .findFirst();

        if (indexOpt.isEmpty()) {
            throw new ContentNotFoundException();
        }

        int index = indexOpt.get();
        Content content = contentList.get(index);

        // Se già approvato, ritorna false
        if (content.isApproved()) {
            return false;
        }

        // Crea nuovo contenuto approvato (immutabile)
        Content approvedContent = new Content(
                content.id(),
                content.name(),
                content.description(),
                ContentState.APPROVED
        );

        // Sostituisce nella lista
        contentList.set(index, approvedContent);
        return true;
    }

    /**
     * Retrieves all approved content suitable for social media publishing.
     * This method filters the internal content storage to return only items
     * that have been approved for public visibility.
     *
     * @return an immutable list containing all approved content items,
     *         empty list if no approved content exists
     */
    public List<Content> showSocialContent() {
        return this.contentList.stream()
                .filter(Content::isApproved)
                .collect(Collectors.toList());
    }

    /**
     * Legacy version of social content retrieval that maintains backward compatibility.
     * Returns null when no approved content exists, preserving the original API contract.
     *
     * @return list of approved content items, or null if no approved content exists
     */
    public List<Content> showSocialContentLegacy() {
        List<Content> approvedContent = showSocialContent();
        return approvedContent.isEmpty() ? null : approvedContent;
    }

    // Metodi di utilità per coerenza

    /**
     * Locates a content item by its unique identifier.
     * Provides safe content lookup with proper null handling through Optional.
     *
     * @param id the unique identifier to search for, must not be null
     * @return Optional containing the content if found, empty Optional otherwise
     * @throws NullPointerException if id is null
     */
    public Optional<Content> findContentById(String id) {
        return contentList.stream()
                .filter(content -> content.id().equals(id))
                .findFirst();
    }

    /**
     * Counts the number of content items in a specific approval state.
     * Useful for generating statistics and monitoring content workflow progress.
     *
     * @param state the content state to count, must not be null
     * @return the number of content items in the specified state
     * @throws NullPointerException if state is null
     */
    public long countContentsByState(ContentState state) {
        return contentList.stream()
                .filter(content -> content.state() == state)
                .count();
    }

    /**
     * Retrieves all content items that are pending approval.
     * Convenience method for workflow management and administrative interfaces.
     *
     * @return immutable list of all pending content items, empty if none exist
     */
    public List<Content> getPendingContent() {
        return contentList.stream()
                .filter(Content::isPending)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all content items that have been rejected.
     * Useful for review processes and content quality analysis.
     *
     * @return immutable list of all rejected content items, empty if none exist
     */
    public List<Content> getRejectedContent() {
        return contentList.stream()
                .filter(Content::isRejected)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a defensive copy of the internal content list.
     * Returns a new ArrayList to prevent external modification of the internal state,
     * maintaining encapsulation and data integrity.
     *
     * @return a new list containing all managed content items
     */
    public List<Content> getContentList() {
        return new ArrayList<>(contentList); // Defensive copy per immutabilità
    }

    /**
     * Replaces the internal content list with a defensive copy of the provided list.
     * Creates a new ArrayList from the input to prevent external modification
     * of the internal state after assignment.
     *
     * @param contentList the new content list to be managed by this service,
     *                    must not be null
     * @throws NullPointerException if contentList is null
     */
    public void setContentList(List<Content> contentList) {
        this.contentList = new ArrayList<>(contentList); // Defensive copy
    }
}