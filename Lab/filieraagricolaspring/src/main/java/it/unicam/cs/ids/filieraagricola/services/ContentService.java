package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.model.Content;
import it.unicam.cs.ids.filieraagricola.model.ContentState;
import it.unicam.cs.ids.filieraagricola.model.ContentType;
import it.unicam.cs.ids.filieraagricola.model.SupplyChainPoint;
import it.unicam.cs.ids.filieraagricola.model.repositories.ContentRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.SupplyChainPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service for managing {@link Content} lifecycle and moderation.
 *
 * <p>Provides retrieval, creation and state transitions (approve/reject) using
 * Spring Data repositories. This service does not perform persistence-level
 * transactions across multiple operations.</p>
 */
@Service
public class ContentService {

    @Autowired
    private ContentRepository contents;
    @Autowired
    private SupplyChainPointRepository supplyChainPointRepository;


    /**
     * Returns all contents.
     */
    public List<Content> getContents() {
        return contents.findAll();
    }


    /**
     * Returns contents filtered by {@link ContentState}.
     */
    public List<Content> getContents(ContentState state) {
        return contents.findByState(state);
    }

    /**
     * Creates a new pending {@link Content} associated to a {@link SupplyChainPoint}.
     *
     * @param name               content title
     * @param certification      content description/body (must not be null/empty)
     * @param type               semantic type
     * @param idSupplyChainPoint target point id
     * @throws IllegalArgumentException if certification is blank or point not found
     */
    public Content addContent(String name, String certification, ContentType type, String idSupplyChainPoint) {
        if (certification == null || certification.trim().isEmpty()) {
            throw new IllegalArgumentException("Certification cannot be null or empty");
        }
        Optional<SupplyChainPoint> opt = supplyChainPointRepository.findById(idSupplyChainPoint);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("The point is not valid");
        }
        String normalizedCert = certification.trim();
        Content content = new Content(null, name, normalizedCert, ContentState.PENDING);
        content.setType(type);
        content.setPoint(opt.get());
        content = contents.save(content);
        return content;
    }

    /**
     * Deletes a content by id if present.
     *
     * @return true if removed, false otherwise
     */
    public boolean removeContent(String id) {
        Content content = getContent(id);
        if (content == null) {
            return false;
        }
        contents.delete(content);
        return true;
    }

    /**
     * Checks whether any content exists.
     */
    public boolean hasCertifications() {
        return contents.count() > 0;
    }

    /**
     * Checks whether a specific content exists (by id).
     */
    public boolean hasCertification(Content certification) {
        if (certification == null) return false;
        return contents.findById(certification.getId()).isPresent();
    }


    /**
     * Returns a content by id or null if not found.
     */
    public Content getContent(String id) {
        Optional<Content> opt = contents.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    /**
     * Sets content state to {@link ContentState#APPROVED} if found.
     */
    public Boolean approve(String id) {
        Optional<Content> opt = contents.findById(id);
        if (opt.isPresent()) {
            Content content = opt.get();
            content.setState(ContentState.APPROVED);
            contents.save(content);
            return true;
        }
        return false;
    }

    /**
     * Sets content state to {@link ContentState#REJECTED} if found.
     */
    public Boolean reject(String id) {
        Optional<Content> opt = contents.findById(id);
        if (opt.isPresent()) {
            Content content = opt.get();
            content.setState(ContentState.REJECTED);
            contents.save(content);
            return true;
        }
        return false;
    }

}
