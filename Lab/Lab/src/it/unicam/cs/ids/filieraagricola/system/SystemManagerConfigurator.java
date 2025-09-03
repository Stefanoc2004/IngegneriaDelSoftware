package it.unicam.cs.ids.filieraagricola.system;

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

    }
}