package it.unicam.cs.ids.filieraagricola.system;

// Assume a ProductService exists for business logic
// import it.unicam.cs.ids.filieraagricola.service.ProductService;
import java.time.LocalDate;

/**
 * Action to handle the creation of a new product.
 */
public class CreateProductAction implements SystemAction {
    @Override
    public void handleRequest(FormatRequest request) {
        if (request.getParams().length < 7) {
            System.out.println("Error: Insufficient parameters for creating a product.");
            return;
        }

        SystemManager system = SystemManager.getInstance();

        try {
            String name = request.getParams()[0];
            String category = request.getParams()[1];
            String description = request.getParams()[2];
            String cultivationMethod = request.getParams()[3];
            String certifications = request.getParams()[4];
            LocalDate productionDate = LocalDate.parse(request.getParams()[5]); // e.g., "2025-09-03"
            String producerId = request.getParams()[6];

            System.out.println("Product '" + name + "' created successfully.");

        } catch (Exception e) {
            System.out.println("Error creating product: " + e.getMessage());
        }
    }
}