package it.unicam.cs.ids.filieraagricola.system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Main method:
 * - gets the singleton instance of SystemManager,
 * - reads the input file line by line,
 * - creates a FormatRequest for each line and sends it to the system,
 * - prints the currently logged user after each request,
 * - handles file reading errors.
 */


public class Main {
    public static void main(String[] args) {
        // Get the singleton instance of the system
        SystemManager system = SystemManager.getInstance();
        SystemManagerConfigurator.configure();
        // Input file: each line represents a request (e.g., "login john 12345")
        String filePath = "input.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Create a FormatRequest from the line
                FormatRequest request = new FormatRequest(line.trim());

                // Send the request to the system
                system.handleRequest(request);

                // Show the currently logged user (if any)
                if (system.getLoggedUser() != null) {
                    System.out.println("Logged user: " +
                            system.getLoggedUser().getUser().getName());
                } else {
                    System.out.println("No user is currently logged in.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading the file: " + e.getMessage());
        }
    }
}