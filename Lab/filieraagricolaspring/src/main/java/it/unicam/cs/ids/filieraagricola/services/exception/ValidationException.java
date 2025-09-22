package it.unicam.cs.ids.filieraagricola.services.exception;

/**
 * Unchecked exception indicating that one or more input values are invalid
 * for the requested operation.
 *
 * <p>Thrown by services and use cases when preconditions or invariants are not met
 * (for example null/empty arguments, invalid state transitions). Centralized here per
 * project rules.</p>
 */
public class ValidationException extends DomainException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ValidationException with no detail message.
     */
    public ValidationException() {
        super();
    }

    /**
     * Creates a new ValidationException with the specified detail message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Creates a new ValidationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new ValidationException with the specified cause.
     *
     * @param cause the cause
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }
}
