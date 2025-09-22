package it.unicam.cs.ids.filieraagricola.model;

/**
 * Lifecycle states for an {@link Order}.
 *
 * <p>Typical progression: <code>PENDING</code> → <code>CONFIRMED</code> →
 * <code>PROCESSING</code> → <code>SHIPPED</code> → <code>DELIVERED</code>.
 * Cancellation can occur from earlier states.</p>
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
