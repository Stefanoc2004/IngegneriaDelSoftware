package it.unicam.cs.ids.filieraagricola.system;

import it.unicam.cs.ids.filieraagricola.service.ContentService;
import it.unicam.cs.ids.filieraagricola.service.exception.NotFoundException;

/*
 * Approves a content item identified by its ID
 */
public class ApproveContentAction implements SystemAction {

    @Override
    public void handleRequest(FormatRequest request) {
        SystemManager system = SystemManager.getInstance();
        ContentService contentService = system.getContentService();

        // Otteniamo l'ID come stringa direttamente, senza convertirlo in int
        String contentId = request.getParams()[0];

        try {
            // Chiamiamo il metodo corretto che esiste realmente in ContentService
            boolean wasApproved = contentService.approveContentById(contentId);

            if (wasApproved) {
                System.out.println("Content with ID " + contentId + " has been approved successfully.");
            } else {
                System.out.println("Content with ID " + contentId + " was already approved.");
            }

        } catch (NotFoundException e) {
            // Catturiamo l'eccezione corretta che viene effettivamente lanciata
            System.out.println("Content with ID " + contentId + " not found: " + e.getMessage());
        }
    }
}