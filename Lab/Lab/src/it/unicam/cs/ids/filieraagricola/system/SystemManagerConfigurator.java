package it.unicam.cs.ids.filieraagricola.system;

import it.unicam.cs.ids.filieraagricola.model.UserRole;
import it.unicam.cs.ids.filieraagricola.service.UserService;

public class SystemManagerConfigurator {
    public static void configure() {
        SystemManager system = SystemManager.getInstance();

        system.addMap("login", new LoginAction());
        system.addMap("anonymousLogin", new AnonymousLoginAction());
        system.addMap("approve_content", new AuthorizeAction(new ApproveContentAction(), "content_curate"));
        system.addMap("create_event", new AuthorizeAction(new CreateEventAction(), "events_manage"));
        system.addMap("create_product", new AuthorizeAction(new CreateProductAction(), "products_manage"));
        system.addMap("register_event", new AuthorizeAction(new RegisterForEventAction(), "events_participate"));
        system.addMap("distributor_create_package", new AuthorizeAction(new DistributorCreatePackageAction(), "distributor_rights"));
        system.addMap("admin_approve_registration", new AuthorizeAction(new ApproveRegistrationAction(), "admin_manage_registrations"));
        system.addMap("create_certificate", new AuthorizeAction(new CreateProductAction(), "certifications"));

        // register prototypes (use the UserRole enum as first parameter)
        // NOTE: adjust permission strings to match your domain vocabulary
        UserService userService = system.getUserService();
        if (userService == null) {
            throw new IllegalStateException("UserService is not initialized in SystemManager. Initialize SystemManager with services before calling SystemManagerConfigurator.configure()");
        }

        userService.registerPrototype("Producer", UserService.makePrototype(UserRole.PRODUCER, "certifications"));
        userService.registerPrototype("Approver", UserService.makePrototype(UserRole.CURATOR, "approve rights"));
    }
}