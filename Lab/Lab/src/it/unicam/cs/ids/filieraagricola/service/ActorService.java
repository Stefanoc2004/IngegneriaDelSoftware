package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing all types of actors in the agricultural supply chain platform.
 *
 * <p>This service provides centralized management for all actor types including producers,
 * transformers, distributors, curators, animators, buyers, general users, and platform managers.
 * It uses defensive copying to ensure data integrity and follows the same patterns as other
 * services in the platform.</p>
 *
 * <p>All actors are stored internally and defensive copies are returned to maintain
 * encapsulation. The service supports operations like registration, authentication,
 * search, and management of actor permissions and status.</p>
 */
public class ActorService {

    private final List<Actor> actorList;
    private final Map<ActorType, List<Actor>> actorsByType;

    /**
     * Constructs an empty ActorService.
     */
    public ActorService() {
        this.actorList = new ArrayList<>();
        this.actorsByType = new EnumMap<>(ActorType.class);

        // Initialize lists for each actor type
        for (ActorType type : ActorType.values()) {
            actorsByType.put(type, new ArrayList<>());
        }
    }

    /**
     * Registers a new actor in the system.
     *
     * <p>The actor is stored as a defensive copy and added to both the main list
     * and the type-specific list for efficient retrieval.</p>
     *
     * @param actor the actor to register (must not be null)
     * @return true if the actor was successfully registered
     * @throws IllegalArgumentException if actor is null or already exists
     */
    public boolean registerActor(Actor actor) {
        Objects.requireNonNull(actor, "Actor cannot be null");

        // Check if actor already exists
        if (findActorById(actor.getId()).isPresent()) {
            throw new IllegalArgumentException("Actor with ID " + actor.getId() + " already exists");
        }

        Actor actorCopy = copyActor(actor);
        actorList.add(actorCopy);
        actorsByType.get(actorCopy.getType()).add(actorCopy);

        return true;
    }

    /**
     * Finds an actor by their unique identifier.
     *
     * @param actorId the actor ID to search for (must not be null)
     * @return Optional containing a defensive copy of the found actor or empty if not found
     * @throws IllegalArgumentException if actorId is null
     */
    public Optional<Actor> findActorById(String actorId) {
        Objects.requireNonNull(actorId, "Actor ID cannot be null");

        return actorList.stream()
                .filter(actor -> actor.getId().equals(actorId))
                .findFirst()
                .map(this::copyActor);
    }

    /**
     * Finds an actor by their email address.
     *
     * @param email the email to search for (must not be null)
     * @return Optional containing a defensive copy of the found actor or empty if not found
     * @throws IllegalArgumentException if email is null
     */
    public Optional<Actor> findActorByEmail(String email) {
        Objects.requireNonNull(email, "Email cannot be null");

        return actorList.stream()
                .filter(actor -> actor.getEmail() != null && actor.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst()
                .map(this::copyActor);
    }

    /**
     * Returns all actors of a specific type.
     *
     * @param actorType the type of actors to retrieve (must not be null)
     * @return list of defensive copies of actors of the specified type
     * @throws IllegalArgumentException if actorType is null
     */
    public List<Actor> getActorsByType(ActorType actorType) {
        Objects.requireNonNull(actorType, "Actor type cannot be null");

        return actorsByType.get(actorType).stream()
                .map(this::copyActor)
                .collect(Collectors.toList());
    }

    /**
     * Returns all active actors of a specific type.
     *
     * @param actorType the type of actors to retrieve (must not be null)
     * @return list of defensive copies of active actors of the specified type
     * @throws IllegalArgumentException if actorType is null
     */
    public List<Actor> getActiveActorsByType(ActorType actorType) {
        Objects.requireNonNull(actorType, "Actor type cannot be null");

        return actorsByType.get(actorType).stream()
                .filter(Actor::isActive)
                .map(this::copyActor)
                .collect(Collectors.toList());
    }

    /**
     * Returns all registered actors.
     *
     * @return list of defensive copies of all actors
     */
    public List<Actor> getAllActors() {
        return actorList.stream()
                .map(this::copyActor)
                .collect(Collectors.toList());
    }

    /**
     * Returns all active actors.
     *
     * @return list of defensive copies of all active actors
     */
    public List<Actor> getAllActiveActors() {
        return actorList.stream()
                .filter(Actor::isActive)
                .map(this::copyActor)
                .collect(Collectors.toList());
    }

    /**
     * Searches for actors by name (case-insensitive partial match).
     *
     * @param name the name or partial name to search for (must not be null or empty)
     * @return list of defensive copies of matching actors
     * @throws IllegalArgumentException if name is null or empty
     */
    public List<Actor> searchActorsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name cannot be null or empty");
        }

        String searchTerm = name.trim().toLowerCase();
        return actorList.stream()
                .filter(actor -> actor.getName() != null &&
                        actor.getName().toLowerCase().contains(searchTerm))
                .map(this::copyActor)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing actor's information.
     *
     * @param updatedActor the actor with updated information (must not be null)
     * @return true if the actor was successfully updated
     * @throws IllegalArgumentException if actor is null or not found
     */
    public boolean updateActor(Actor updatedActor) {
        Objects.requireNonNull(updatedActor, "Updated actor cannot be null");

        Optional<Integer> indexOpt = findActorIndex(updatedActor.getId());
        if (indexOpt.isEmpty()) {
            throw new IllegalArgumentException("Actor with ID " + updatedActor.getId() + " not found");
        }

        int index = indexOpt.get();
        Actor oldActor = actorList.get(index);
        Actor newActorCopy = copyActor(updatedActor);

        // Update in main list
        actorList.set(index, newActorCopy);

        // Update in type-specific list
        List<Actor> typeList = actorsByType.get(oldActor.getType());
        int typeIndex = typeList.indexOf(oldActor);
        if (typeIndex >= 0) {
            typeList.set(typeIndex, newActorCopy);
        }

        // If type changed, move to new type list
        if (oldActor.getType() != newActorCopy.getType()) {
            actorsByType.get(oldActor.getType()).remove(oldActor);
            actorsByType.get(newActorCopy.getType()).add(newActorCopy);
        }

        return true;
    }

    /**
     * Deactivates an actor (sets active status to false).
     *
     * @param actorId the ID of the actor to deactivate (must not be null)
     * @return true if the actor was successfully deactivated
     * @throws IllegalArgumentException if actorId is null or actor not found
     */
    public boolean deactivateActor(String actorId) {
        Objects.requireNonNull(actorId, "Actor ID cannot be null");

        Optional<Actor> actorOpt = findActorById(actorId);
        if (actorOpt.isEmpty()) {
            throw new IllegalArgumentException("Actor with ID " + actorId + " not found");
        }

        Actor actor = actorOpt.get();
        actor.setActive(false);
        return updateActor(actor);
    }

    /**
     * Activates an actor (sets active status to true).
     *
     * @param actorId the ID of the actor to activate (must not be null)
     * @return true if the actor was successfully activated
     * @throws IllegalArgumentException if actorId is null or actor not found
     */
    public boolean activateActor(String actorId) {
        Objects.requireNonNull(actorId, "Actor ID cannot be null");

        Optional<Actor> actorOpt = findActorById(actorId);
        if (actorOpt.isEmpty()) {
            throw new IllegalArgumentException("Actor with ID " + actorId + " not found");
        }

        Actor actor = actorOpt.get();
        actor.setActive(true);
        return updateActor(actor);
    }

    /**
     * Removes an actor from the system.
     *
     * @param actorId the ID of the actor to remove (must not be null)
     * @return true if the actor was successfully removed
     * @throws IllegalArgumentException if actorId is null
     */
    public boolean removeActor(String actorId) {
        Objects.requireNonNull(actorId, "Actor ID cannot be null");

        Optional<Integer> indexOpt = findActorIndex(actorId);
        if (indexOpt.isEmpty()) {
            return false;
        }

        int index = indexOpt.get();
        Actor actor = actorList.get(index);

        // Remove from main list
        actorList.remove(index);

        // Remove from type-specific list
        actorsByType.get(actor.getType()).remove(actor);

        return true;
    }

    /**
     * Returns the count of actors by type.
     *
     * @return map containing the count of actors for each type
     */
    public Map<ActorType, Long> getActorCountByType() {
        return actorList.stream()
                .collect(Collectors.groupingBy(
                        Actor::getType,
                        () -> new EnumMap<>(ActorType.class),
                        Collectors.counting()
                ));
    }

    /**
     * Returns the count of active actors by type.
     *
     * @return map containing the count of active actors for each type
     */
    public Map<ActorType, Long> getActiveActorCountByType() {
        return actorList.stream()
                .filter(Actor::isActive)
                .collect(Collectors.groupingBy(
                        Actor::getType,
                        () -> new EnumMap<>(ActorType.class),
                        Collectors.counting()
                ));
    }

    /**
     * Authenticates an actor by email and returns their information if valid.
     *
     * @param email the email address (must not be null)
     * @return Optional containing a defensive copy of the authenticated actor or empty if not found/inactive
     * @throws IllegalArgumentException if email is null
     */
    public Optional<Actor> authenticateActor(String email) {
        Objects.requireNonNull(email, "Email cannot be null");

        return actorList.stream()
                .filter(actor -> actor.getEmail() != null &&
                        actor.getEmail().equalsIgnoreCase(email.trim()) &&
                        actor.isActive())
                .findFirst()
                .map(this::copyActor);
    }

    /**
     * Returns actors that can upload content to the platform.
     *
     * @return list of defensive copies of actors with content upload permissions
     */
    public List<Actor> getContentUploadCapableActors() {
        return actorList.stream()
                .filter(Actor::canUploadContent)
                .map(this::copyActor)
                .collect(Collectors.toList());
    }

    /**
     * Returns actors that can approve content on the platform.
     *
     * @return list of defensive copies of actors with content approval permissions
     */
    public List<Actor> getContentApprovalCapableActors() {
        return actorList.stream()
                .filter(Actor::canApproveContent)
                .map(this::copyActor)
                .collect(Collectors.toList());
    }

    /**
     * Returns actors that can organize events on the platform.
     *
     * @return list of defensive copies of actors with event organization permissions
     */
    public List<Actor> getEventOrganizationCapableActors() {
        return actorList.stream()
                .filter(Actor::canOrganizeEvents)
                .map(this::copyActor)
                .collect(Collectors.toList());
    }

    /**
     * Returns actors that can make purchases on the platform.
     *
     * @return list of defensive copies of actors with purchase permissions
     */
    public List<Actor> getPurchaseCapableActors() {
        return actorList.stream()
                .filter(Actor::canMakePurchases)
                .map(this::copyActor)
                .collect(Collectors.toList());
    }

    // ----------------- private helpers -----------------

    /**
     * Creates a defensive copy of an actor using the clone method.
     *
     * @param actor the actor to copy (must not be null)
     * @return a cloned copy of the actor
     */
    private Actor copyActor(Actor actor) {
        Objects.requireNonNull(actor, "Actor cannot be null");
        return actor.clone();
    }

    /**
     * Finds the index of an actor in the main list by ID.
     *
     * @param actorId the actor ID to search for
     * @return Optional containing the index or empty if not found
     */
    private Optional<Integer> findActorIndex(String actorId) {
        for (int i = 0; i < actorList.size(); i++) {
            if (actorList.get(i).getId().equals(actorId)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }
}
