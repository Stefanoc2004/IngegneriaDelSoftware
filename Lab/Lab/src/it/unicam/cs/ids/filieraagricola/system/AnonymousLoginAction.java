package it.unicam.cs.ids.filieraagricola.system;

public class AnonymousLoginAction implements SystemAction {
    /*
    * performs an anonymous login
     */
    @Override
    public void handleRequest(FormatRequest request) {
        SystemManager system = SystemManager.getInstance();
        system.anonymousLogin();
    }
}
