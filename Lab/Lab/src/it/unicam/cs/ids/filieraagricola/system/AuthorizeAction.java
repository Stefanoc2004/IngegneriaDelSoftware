package it.unicam.cs.ids.filieraagricola.system;

public class AuthorizeAction implements SystemAction {


    private SystemAction systemAction;
    private String[] permissions;

    public AuthorizeAction(SystemAction systemAction, String... permissions) {
        this.permissions = permissions;
        this.systemAction = systemAction;
    }

    @Override
    public void handleRequest(FormatRequest request) {
        SystemManager system = SystemManager.getInstance();
        String[] permissions = system.getPermissions();
        //TODO verificare se i permessi richiesti dall'azione esistono nei permessi dell'utente, se non esistono non viene eseguita nessun azione
        boolean approve = true;
        if (approve){
            this.systemAction.handleRequest(request);
        }
    }
}
