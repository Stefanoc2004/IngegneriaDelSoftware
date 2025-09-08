package it.unicam.cs.ids.filieraagricola.service.exception;

/**
 * Base unchecked exception for all domain-related errors in the application.
 *
 * <p>All service-layer and use-case specific exceptions should extend this class
 * to allow centralized handling and consistent error semantics across the system.</p>
 *
 * <p>This hierarchy follows project rules to centralize exceptions under
 * {@code it.unicam.cs.ids.filieraagricola.service.exception}.</p>
 */
public class DomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new DomainException with no detail message.
     */
    public DomainException() {
        super();
    }

    /**
     * Creates a new DomainException with the specified detail message.
     *
     * @param message the detail message
     */
    public DomainException(String message) {
        super(message);
    }

    /**
     * Creates a new DomainException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new DomainException with the specified cause.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public DomainException(Throwable cause) {
        super(cause);
    }
}
