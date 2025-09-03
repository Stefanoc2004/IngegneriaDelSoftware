package it.unicam.cs.ids.filieraagricola.system;


/**
 * Action to handle the approval of a new actor registration by a Platform Manager.
 */
public class ApproveRegistrationAction implements SystemAction {
    @Override
    public void handleRequest(FormatRequest request) {
        if (request.getParams().length < 1) {
            System.out.println("Error: Actor ID is required for approval.");
            return;
        }

        SystemManager system = SystemManager.getInstance();
        if (system.getLoggedUser() == null) {
            System.out.println("Error: You must be logged in as a Platform Manager.");
            return;
        }

        try {
            String actorIdToApprove = request.getParams()[0];
            String managerId = system.getLoggedUser().getUser().getEmail();

            System.out.println("Registration for actor '" + actorIdToApprove + "' approved by manager '" + managerId + "'.");

        } catch (Exception e) {
            System.out.println("Error during registration approval: " + e.getMessage());
        }
    }
}