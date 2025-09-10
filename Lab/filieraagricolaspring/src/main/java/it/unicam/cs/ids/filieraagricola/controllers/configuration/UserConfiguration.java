package it.unicam.cs.ids.filieraagricola.controllers.configuration;

import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.model.UserRole;
import it.unicam.cs.ids.filieraagricola.services.UserPrototypeRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UserConfiguration {
    public static void registerDefaultPrototypes(UserPrototypeRegistry registry) {
        // Producer: can create content and list products
        User producer = new User();
        producer.setPermissions(UserRole.PRODUCER, UserRole.GENERIC_USER);
//        producer.setPermissions(new String[] {"CREATE_CONTENT", "LIST_PRODUCTS", "VIEW_MAP"});
        registry.registerPrototype("producer", producer);

        // Transformer: create content about transformation and link producers
        User transformer = new User();
        transformer.setPermissions(UserRole.TRANSFORMER);
//        transformer.setPermissions(new String[] {"CREATE_CONTENT", "LINK_PRODUCER", "LIST_PRODUCTS"});
        registry.registerPrototype("transformer", transformer);

        // Distributor: create product listings and packages
        User distributor = new User();
        distributor.setPermissions(UserRole.DISTRIBUTOR);
//        distributor.setPermissions(new String[] {"CREATE_CONTENT", "CREATE_PACKAGE", "LIST_PRODUCTS"});
        registry.registerPrototype("distributor", distributor);

        // Curator: approves content
        User curator = new User();
        curator.setPermissions(UserRole.CURATOR);
//        curator.setPermissions(new String[] {"APPROVE_CONTENT", "MANAGE_CONTENT"});
        registry.registerPrototype("curator", curator);

        // Animator: organizes events
        User animator = new User();
        animator.setPermissions(UserRole.ANIMATOR);
//        animator.setPermissions(new String[] {"ORGANIZE_EVENT", "INVITE_PARTICIPANTS"});
        registry.registerPrototype("animator", animator);

        // Buyer: purchase and book
        User buyer = new User();
        buyer.setPermissions(UserRole.BUYER);
//        buyer.setPermissions(new String[] {"PURCHASE", "BOOK_EVENT"});
        registry.registerPrototype("buyer", buyer);

        // Generic user
        User generic = new User();
        generic.setPermissions(UserRole.GENERIC_USER);
//        generic.setPermissions(new String[] {"VIEW_CONTENT", "SHARE_SOCIAL"});
        registry.registerPrototype("generic", generic);

        // Platform manager
        User manager = new User();
        manager.setPermissions(UserRole.PLATFORM_MANAGER);
//        manager.setPermissions(new String[] {"MANAGE_USERS", "MANAGE_SYSTEM", "GRANT_ROLES"});
        registry.registerPrototype("manager", manager);
    }

    @Bean
    public UserPrototypeRegistry getRegistry() {
        UserPrototypeRegistry registry = new UserPrototypeRegistry();
        registerDefaultPrototypes(registry);
        return registry;
    }
}
