package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.model.*;
import it.unicam.cs.ids.filieraagricola.model.repositories.ContentRepository;
import it.unicam.cs.ids.filieraagricola.model.repositories.SupplayChainPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contents;
    @Autowired
    private SupplayChainPointRepository supplayChainPointRepository;



    /**
     * Returns the list of certifications held by this producer.
     *
     * @return defensive copy of certifications list
     */
    public List<Content> getContents() {
        return contents.findAll();
    }


    public List<Content> getContents(ContentState state) {
        return contents.findByState(state);
    }

    /**
     * Adds a certification to this producer's certifications.
     *
     * @param certification certification to add (must not be null or empty)
     * @throws IllegalArgumentException if certification is null or empty
     */
    public void addContent(String name, String certification, ContentType type, String idSupplyChainPoint) {
        if (certification == null || certification.trim().isEmpty()) {
            throw new IllegalArgumentException("Certification cannot be null or empty");
        }
        Optional<SupplyChainPoint> opt = supplayChainPointRepository.findById(idSupplyChainPoint);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("The point is not valid");
        }
        String normalizedCert = certification.trim();
        Content content = new Content(null, name, normalizedCert, ContentState.PENDING);
        content.setType(type);
        content.setPoint(opt.get());
        contents.save(content);
    }
    /**
     * Removes a certification from this producer's certifications.
     *
     * @return true if the certification was removed, false if it wasn't found
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
     * Checks if this producer has any certifications.
     *
     * @return true if the producer has at least one certification
     */
    public boolean hasCertifications() {
        return contents.count() > 0;
    }

    /**
     * Checks if this producer has a specific certification.
     *
     * @param certification certification to check for
     * @return true if the producer has the specified certification
     */
    public boolean hasCertification(Content certification) {
        if (certification == null) return false;
        return contents.findById(certification.getId()).isPresent();
    }


    public Content getContent(String id) {
        Optional<Content> opt = contents.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

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
