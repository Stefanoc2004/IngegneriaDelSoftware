package it.unicam.cs.ids.filieraagricola.system;

public class SystemManagerConfigurator {
    public static void configure() {
        SystemManager system = SystemManager.getInstance();
        system.addMap("login", new LoginAction());
        system.addMap("anonymousLogin", new AnonymousLoginAction());
        system.addMap("approve", new AuthorizeAction(new ApproveContentAction(), "approve rights"));
        system.addMap("create event", new CreateEventAction());
        system.addMap("create event", new AuthorizeAction(new CreateEventAction(), "events rights"));
    }
}
