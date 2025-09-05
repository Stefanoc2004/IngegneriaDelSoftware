package it.unicam.cs.ids.filieraagricola.system.contents;

import it.unicam.cs.ids.filieraagricola.system.FormatRequest;
import it.unicam.cs.ids.filieraagricola.system.SystemAction;
import it.unicam.cs.ids.filieraagricola.system.SystemManager;

public class CreateCertificationAction implements SystemAction {


    @Override
    public void handleRequest(FormatRequest request) {
        SystemManager system = SystemManager.getInstance();
        system.getCertificationService().addCertification(request.getParams()[0],request.getParams()[1]);
    }
}
