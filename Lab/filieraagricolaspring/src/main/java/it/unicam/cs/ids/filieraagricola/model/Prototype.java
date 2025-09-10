package it.unicam.cs.ids.filieraagricola.model;

/**
 * Generic Prototype interface used to implement the Prototype design pattern.
 *
 * <p>Classes implementing {@code Prototype<T>} must provide a {@code clone()}
 * method that returns an instance of {@code T}. Using a generic interface
 * avoids raw-type casts and allows covariant return types in concrete classes
 * (e.g. {@code public Product clone()}).</p>
 *
 * @param <T> concrete type returned by {@link #clone()}
 */
public interface Prototype<T> {
    /**
     * Create and return a deep copy of this object.
     *
     * @return a cloned instance of type T
     */
    T clone();
}
