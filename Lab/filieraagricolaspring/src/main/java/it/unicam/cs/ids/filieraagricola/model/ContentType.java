package it.unicam.cs.ids.filieraagricola.model;

/**
 * Semantic classification for {@link Content} items.
 *
 * <p>Values are used to drive filtering and UI presentation logic. The
 * enumeration may be extended as new domain content types are introduced.</p>
 */
public enum ContentType {

    /** Certification documents, labels, quality marks. */
    CERTIFICATE,
    /** Agronomic methods, cultivation guidelines and practices. */
    CULTIVATION_PROCEDURE,
    /** Processing/production steps and related documentation. */
    PRODUCTION_PROCEDURE

}
