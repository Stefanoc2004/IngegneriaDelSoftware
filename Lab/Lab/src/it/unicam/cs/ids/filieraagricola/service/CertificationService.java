package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Content;
import it.unicam.cs.ids.filieraagricola.model.ContentState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CertificationService {

    private List<Content> certifications;

    public CertificationService() {
        this.certifications = new LinkedList<>();
    }

    /**
     * Sets the certifications for this producer.
     *
     * @param certifications list of certifications (may be null, will be treated as empty)
     */
    public void setCertifications(List<Content> certifications) {
        this.certifications = certifications != null ? new ArrayList<>(certifications) : new ArrayList<>();
    }

    /**
     * Returns the list of certifications held by this producer.
     *
     * @return defensive copy of certifications list
     */
    public List<Content> getCertifications() {
        return new ArrayList<>(certifications);
    }

    /**
     * Adds a certification to this producer's certifications.
     *
     * @param certification certification to add (must not be null or empty)
     * @throws IllegalArgumentException if certification is null or empty
     */
    public void addCertification(String name, String certification) {
        if (certification == null || certification.trim().isEmpty()) {
            throw new IllegalArgumentException("Certification cannot be null or empty");
        }
        String normalizedCert = certification.trim();
        Content content = new Content(certifications.size()+"", name, normalizedCert, ContentState.PENDING);
        certifications.add(content);
    }
    /**
     * Removes a certification from this producer's certifications.
     *
     * @param certification certification to remove
     * @return true if the certification was removed, false if it wasn't found
     */
    public boolean removeCertification(Content certification) {
        if (certification == null) return false;
        return certifications.remove(certification);
    }

    /**
     * Checks if this producer has any certifications.
     *
     * @return true if the producer has at least one certification
     */
    public boolean hasCertifications() {
        return !certifications.isEmpty();
    }

    /**
     * Checks if this producer has a specific certification.
     *
     * @param certification certification to check for
     * @return true if the producer has the specified certification
     */
    public boolean hasCertification(Content certification) {
        if (certification == null) return false;
        return certifications.contains(certification);
    }

}
