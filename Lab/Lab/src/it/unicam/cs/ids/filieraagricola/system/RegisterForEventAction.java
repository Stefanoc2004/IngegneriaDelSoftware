package it.unicam.cs.ids.filieraagricola.system;


/**
 * Action to handle user registration for an event.
 */
public class RegisterForEventAction implements SystemAction {
    @Override
    public void handleRequest(FormatRequest request) {
        if (request.getParams().length < 2) {
            System.out.println("Error: Event ID and role are required.");
            return;
        }

        SystemManager system = SystemManager.getInstance();
        if (system.getLoggedUser() == null) {
            System.out.println("Error: You must be logged in to register for an event.");
            return;
        }

        try {
            String eventId = request.getParams()[0];
            String role = request.getParams()[1].toUpperCase(); // e.g., VISITOR
            String actorId = system.getLoggedUser().getUser().getEmail(); // Use email as actor ID

            System.out.println("User " + actorId + " successfully registered for event " + eventId + " as " + role);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid role provided. Must be ORGANIZER, EXHIBITOR, SPEAKER, or VISITOR.");
        } catch (Exception e) {
            System.out.println("Error during event registration: " + e.getMessage());
        }
    }
}