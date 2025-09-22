package it.unicam.cs.ids.filieraagricola.model;

/**
 * Approval workflow states for {@link Content}.
 *
 * <p>States reflect the moderation process handled by curators:</p>
 * <ul>
 *   <li><code>PENDING</code> — newly created content awaiting review.</li>
 *   <li><code>APPROVED</code> — content verified and visible to end users.</li>
 *   <li><code>REJECTED</code> — content not accepted; may require changes.</li>
 * </ul>
 */
public enum ContentState {
    PENDING,
    APPROVED,
    REJECTED
}
