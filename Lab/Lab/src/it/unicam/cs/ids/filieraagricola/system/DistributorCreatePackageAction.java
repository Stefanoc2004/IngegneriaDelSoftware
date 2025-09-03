package it.unicam.cs.ids.filieraagricola.system;

import java.util.Arrays;
import java.util.List;

/**
 * Action to handle the creation of a product package by a Distributor.
 */
public class DistributorCreatePackageAction implements SystemAction {
    @Override
    public void handleRequest(FormatRequest request) {

        if (request.getParams().length < 2) {
            System.out.println("Error: Package name and at least one product ID are required.");
            return;
        }

        SystemManager system = SystemManager.getInstance();
        if (system.getLoggedUser() == null) {
            System.out.println("Error: You must be logged in as a Distributor.");
            return;
        }

        try {
            String packageName = request.getParams()[0];
            List<String> productIds = Arrays.asList(
                    Arrays.copyOfRange(request.getParams(), 1, request.getParams().length)
            );
            String distributorId = system.getLoggedUser().getUser().getEmail(); ;

            System.out.println("Package '" + packageName + "' created successfully by " + distributorId);
            System.out.println("Products included: " + productIds);

        } catch (Exception e) {
            System.out.println("Error creating package: " + e.getMessage());
        }
    }
}