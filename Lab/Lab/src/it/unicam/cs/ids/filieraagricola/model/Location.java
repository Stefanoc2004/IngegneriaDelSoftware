package it.unicam.cs.ids.filieraagricola.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a geographical location within the agricultural supply chain platform.
 *
 * <p>This class models a location with a unique identifier, name, address, and optional description.
 * It is a pure data model without business logic.
 * Validation and business rules related to locations are handled by {@link it.unicam.cs.ids.filieraagricola.service.LocationService}.</p>
 */
public class Location {

    /**
     * Unique identifier for the location.
     */
    private final String id;

    /**
     * Name of the location.
     */
    private final String name;

    /**
     * Address of the location.
     */
    private final String address;

    /**
     * Optional description of the location.
     */
    private final String description;

    /**
     * Constructs a new Location instance with a generated unique identifier.
     *
     * @param name        The name of the location.
     * @param address     The address of the location.
     * @param description Optional description of the location.
     */
    public Location(String name, String address, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.description = description;
    }

    /**
     * Constructs a new Location instance with a specified identifier.
     * Typically used when reconstructing from persistence.
     *
     * @param id          The unique identifier of the location.
     * @param name        The name of the location.
     * @param address     The address of the location.
     * @param description Optional description of the location.
     */
    public Location(String id, String name, String address, String description) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
    }

    /**
     * Returns the unique identifier of this location.
     *
     * @return The location ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of this location.
     *
     * @return The location name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the address of this location.
     *
     * @return The location address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the optional description of this location.
     *
     * @return The location description, or null if none.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Indicates whether some other object is "equal to" this Location.
     * Two Location objects are considered equal if they have the same ID.
     *
     * @param o The reference object with which to compare.
     * @return True if this object is equal to the o argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id);
    }

    /**
     * Returns a hash code value for this Location.
     * The hash code is based on the location ID.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a string representation of this Location.
     *
     * @return A string representation including id, name, and address.
     */
    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
