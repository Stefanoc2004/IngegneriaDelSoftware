package it.unicam.cs.ids.filieraagricola.services;

import it.unicam.cs.ids.filieraagricola.model.Content;
import it.unicam.cs.ids.filieraagricola.model.ContentState;
import it.unicam.cs.ids.filieraagricola.model.ContentType;
import it.unicam.cs.ids.filieraagricola.model.Event;
import it.unicam.cs.ids.filieraagricola.model.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentService {

    @Autowired
    private ContentRepository certifications;


    /**
     * Returns the list of certifications held by this producer.
     *
     * @return defensive copy of certifications list
     */
    public List<Content> getCertifications() {
        return certifications.findAll();
    }

    /**
     * Adds a certification to this producer's certifications.
     *
     * @param certification certification to add (must not be null or empty)
     * @throws IllegalArgumentException if certification is null or empty
     */
    public void addContent(String name, String certification, ContentType type) {
        if (certification == null || certification.trim().isEmpty()) {
            throw new IllegalArgumentException("Certification cannot be null or empty");
        }
        String normalizedCert = certification.trim();
        Content content = new Content(null, name, normalizedCert, ContentState.PENDING);
        content.setType(type);
        certifications.save(content);
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
        certifications.delete(content);
        return true;
    }

    /**
     * Checks if this producer has any certifications.
     *
     * @return true if the producer has at least one certification
     */
    public boolean hasCertifications() {
        return certifications.count() > 0;
    }

    /**
     * Checks if this producer has a specific certification.
     *
     * @param certification certification to check for
     * @return true if the producer has the specified certification
     */
    public boolean hasCertification(Content certification) {
        if (certification == null) return false;
        return certifications.findById(certification.getId()).isPresent();
    }


    public Content getContent(String id) {
        Optional<Content> opt = certifications.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

}
