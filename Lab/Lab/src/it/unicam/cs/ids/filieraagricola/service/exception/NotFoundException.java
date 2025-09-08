package it.unicam.cs.ids.filieraagricola.service.exception;

/**
 * Unchecked exception indicating that a requested domain resource
 * could not be found.
 *
 * <p>This serves as the base class for more specific not-found exceptions
 * (e.g., content, user, order). Centralized under the service.exception
 * package per project rules.</p>
 */
public class NotFoundException extends DomainException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new NotFoundException with no detail message.
     */
    public NotFoundException() {
        super();
    }

    /**
     * Creates a new NotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new NotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new NotFoundException with the specified cause.
     *
     * @param cause the cause
     */
    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
