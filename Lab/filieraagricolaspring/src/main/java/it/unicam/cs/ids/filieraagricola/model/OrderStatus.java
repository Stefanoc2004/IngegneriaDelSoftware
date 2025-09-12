package it.unicam.cs.ids.filieraagricola.model;

/**
 * Represents the possible states of an order in the agricultural supply chain platform.
 *
 * <p>Orders progress through various states from initial creation to final delivery
 * or cancellation. This enum defines all possible order states and their typical
 * progression flow.</p>
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
