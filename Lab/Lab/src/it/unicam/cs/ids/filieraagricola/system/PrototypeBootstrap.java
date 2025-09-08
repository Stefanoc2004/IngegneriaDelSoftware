package it.unicam.cs.ids.filieraagricola.system;

import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.model.UserRole;

/**
 * Registers default role prototypes into a {@link UserPrototypeRegistry}.
 *
 * <p>This helper is intended to be invoked at application bootstrap (for example
 * in a Spring {@code ApplicationRunner}) to pre-populate common actor prototypes
 * with base permissions. Adjust the permission strings to reflect your domain.</p>
 */
public final class PrototypeBootstrap {

    private PrototypeBootstrap() { /* static helper */ }

    /**
     * Register a set of default actor prototypes useful for development/demo.
     *
     * @param registry registry to populate (must not be null)
     */
    public static void registerDefaultPrototypes(UserPrototypeRegistry registry) {
        // Producer: can create content and list products
        User producer = new User();
        producer.setRole(UserRole.PRODUCER);
        producer.setPermissions(new String[] {"CREATE_CONTENT", "LIST_PRODUCTS", "VIEW_MAP"});
        registry.registerPrototype("producer", producer);

        // Transformer: create content about transformation and link producers
        User transformer = new User();
        transformer.setRole(UserRole.TRANSFORMER);
        transformer.setPermissions(new String[] {"CREATE_CONTENT", "LINK_PRODUCER", "LIST_PRODUCTS"});
        registry.registerPrototype("transformer", transformer);

        // Distributor: create product listings and packages
        User distributor = new User();
        distributor.setRole(UserRole.DISTRIBUTOR);
        distributor.setPermissions(new String[] {"CREATE_CONTENT", "CREATE_PACKAGE", "LIST_PRODUCTS"});
        registry.registerPrototype("distributor", distributor);

        // Curator: approves content
        User curator = new User();
        curator.setRole(UserRole.CURATOR);
        curator.setPermissions(new String[] {"APPROVE_CONTENT", "MANAGE_CONTENT"});
        registry.registerPrototype("curator", curator);

        // Animator: organizes events
        User animator = new User();
        animator.setRole(UserRole.ANIMATOR);
        animator.setPermissions(new String[] {"ORGANIZE_EVENT", "INVITE_PARTICIPANTS"});
        registry.registerPrototype("animator", animator);

        // Buyer: purchase and book
        User buyer = new User();
        buyer.setRole(UserRole.BUYER);
        buyer.setPermissions(new String[] {"PURCHASE", "BOOK_EVENT"});
        registry.registerPrototype("buyer", buyer);

        // Generic user
        User generic = new User();
        generic.setRole(UserRole.GENERIC_USER);
        generic.setPermissions(new String[] {"VIEW_CONTENT", "SHARE_SOCIAL"});
        registry.registerPrototype("generic", generic);

        // Platform manager
        User manager = new User();
        manager.setRole(UserRole.PLATFORM_MANAGER);
        manager.setPermissions(new String[] {"MANAGE_USERS", "MANAGE_SYSTEM", "GRANT_ROLES"});
        registry.registerPrototype("manager", manager);
    }
}
