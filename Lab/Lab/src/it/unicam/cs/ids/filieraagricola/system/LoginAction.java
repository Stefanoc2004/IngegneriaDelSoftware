package it.unicam.cs.ids.filieraagricola.system;

public class LoginAction implements SystemAction {
    @Override
    public void handleRequest(FormatRequest request) {
        SystemManager system = SystemManager.getInstance();

        if (request.getParams().length == 2) {
            system.login(request.getParams()[0], request.getParams()[1]);
        } else {
            System.out.println("Insufficient parameters for login");
        }
    }
}
