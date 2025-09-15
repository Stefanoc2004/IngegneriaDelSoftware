package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Location;
import it.unicam.cs.ids.filieraagricola.service.exception.NotFoundException;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Service class responsible for managing locations within the agricultural supply chain platform.
 *
 * <p>This service handles creation, retrieval, updating, and deletion of {@link Location} objects.
 * It performs validation and ensures data integrity.
 * Collections returned by this service are unmodifiable to preserve encapsulation.</p>
 */
public class LocationService {

    private final List<Location> locationList;

    /**
     * Constructs an empty LocationService.
     */
    public LocationService() {
        this.locationList = new ArrayList<>();
    }

    /**
     * Creates and adds a new location to the system.
     *
     * @param name        The name of the location.
     * @param address     The address of the location.
     * @param description Optional description of the location.
     * @return The newly created Location instance.
     * @throws ValidationException If name or address is null or empty.
     */
    public Location createLocation(String name, String address, String description) {
        validateLocationData(name, address);
        Location location = new Location(name.trim(), address.trim(), description != null ? description.trim() : null);
        locationList.add(location);
        return location;
    }

    /**
     * Finds a location by its unique identifier.
     *
     * @param id The location ID.
     * @return An Optional containing the found location or empty if not found.
     * @throws ValidationException If id is null or empty.
     */
    public Optional<Location> findLocationById(String id) {
        validateId(id);
        return locationList.stream()
                .filter(location -> location.getId().equals(id))
                .findFirst();
    }

    /**
     * Updates an existing location identified by id.
     *
     * @param id          The location ID.
     * @param name        The new name of the location.
     * @param address     The new address of the location.
     * @param description The new description of the location.
     * @return The updated Location instance.
     * @throws ValidationException If id, name, or address is null or empty.
     * @throws NotFoundException   If no location with the given id exists.
     */
    public Location updateLocation(String id, String name, String address, String description) {
        validateId(id);
        validateLocationData(name, address);

        Optional<Integer> indexOpt = findLocationIndex(id);
        if (indexOpt.isEmpty()) {
            throw new NotFoundException("Location with ID " + id + " not found.");
        }

        int index = indexOpt.get();
        Location updatedLocation = new Location(id, name.trim(), address.trim(), description != null ? description.trim() : null);
        locationList.set(index, updatedLocation);
        return updatedLocation;
    }

    /**
     * Deletes a location by its unique identifier.
     *
     * @param id The location ID.
     * @return True if the location was removed, false otherwise.
     * @throws ValidationException If id is null or empty.
     * @throws NotFoundException   If no location with the given id exists.
     */
    public boolean deleteLocation(String id) {
        validateId(id);
        Optional<Location> locationOpt = findLocationById(id);
        if (locationOpt.isEmpty()) {
            throw new NotFoundException("Location with ID " + id + " not found.");
        }
        return locationList.remove(locationOpt.get());
    }

    /**
     * Returns an unmodifiable list of all locations.
     *
     * @return Unmodifiable list of locations.
     */
    public List<Location> getAllLocations() {
        return Collections.unmodifiableList(new ArrayList<>(locationList));
    }

    /**
     * Validates location name and address.
     *
     * @param name    The location name.
     * @param address The location address.
     * @throws ValidationException If name or address is null or empty.
     */
    private void validateLocationData(String name, String address) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Location name cannot be null or empty.");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new ValidationException("Location address cannot be null or empty.");
        }
    }

    /**
     * Validates that the location ID is not null or empty.
     *
     * @param id The location ID.
     * @throws ValidationException If id is null or empty.
     */
    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("Location ID cannot be null or empty.");
        }
    }

    /**
     * Finds the index of a location in the internal list by its ID.
     *
     * @param id The location ID.
     * @return An Optional containing the index or empty if not found.
     */
    private Optional<Integer> findLocationIndex(String id) {
        return IntStream.range(0, locationList.size())
                .filter(i -> locationList.get(i).getId().equals(id))
                .boxed()
                .findFirst();
    }
}
