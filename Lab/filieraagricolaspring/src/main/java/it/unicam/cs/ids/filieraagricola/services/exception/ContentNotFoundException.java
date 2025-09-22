package it.unicam.cs.ids.filieraagricola.services.exception;

/**
 * Specific not-found exception for {@link it.unicam.cs.ids.filieraagricola.model.Content} resources.
 *
 * <p>Prefer using this subclass over the generic {@link NotFoundException} when
 * handling content-related lookups at the service layer.</p>
 */
public class ContentNotFoundException extends NotFoundException {
    /** Creates the exception with a default message. */
    public ContentNotFoundException() {
        super("Content not found");
    }

    /** Creates the exception with a custom message. */
    public ContentNotFoundException(String message) {
        super(message);
    }
}
