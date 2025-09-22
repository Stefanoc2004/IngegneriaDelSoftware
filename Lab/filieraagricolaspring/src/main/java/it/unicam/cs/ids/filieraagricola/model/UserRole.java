package it.unicam.cs.ids.filieraagricola.model;
/**
 * Enumerates user roles (actor types) in the agricultural supply-chain platform.
 *
 * <p>Values correspond to the domain actors defined in the specifications and
 * are used across authorization, content creation and marketplace features.</p>
 */
public enum UserRole {
    PRODUCER,
    TRANSFORMER,
    DISTRIBUTOR,
    CURATOR,
    ANIMATOR,
    BUYER,
    GENERIC_USER,
    PLATFORM_MANAGER
}
