package it.unicam.cs.ids.filieraagricola.model;

/**
 * Generic Prototype interface used to implement the Prototype design pattern.
 *
 * <p>Implementors provide a type-safe <code>clone</code> method returning the
 * concrete type, enabling copying without raw casts.</p>
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
