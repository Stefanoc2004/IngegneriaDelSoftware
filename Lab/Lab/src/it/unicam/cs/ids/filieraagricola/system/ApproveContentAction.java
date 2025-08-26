package it.unicam.cs.ids.filieraagricola.system;

import it.unicam.cs.ids.filieraagricola.service.ContentService;
import it.unicam.cs.ids.filieraagricola.service.exception.ContentNotFoundException;

public class ApproveContentAction implements SystemAction {

    @Override
    public void handleRequest(FormatRequest request) {
        SystemManager system = SystemManager.getInstance();
        ContentService contentService = system.getContentService();
        String idString = request.getParams()[0];
        int id = Integer.parseInt(idString);
        try {
            contentService.approveContent(id);
        } catch (ContentNotFoundException e) {
            System.out.println(id + " Id not found");
        }
    }
}
