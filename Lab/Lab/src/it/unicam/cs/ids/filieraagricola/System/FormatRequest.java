package it.unicam.cs.ids.filieraagricola.system;

public class FormatRequest {
    private String method;
    private String[] params;

    /**
     * FormatRequest class:
     * - parses a raw input line into a command and its parameters,
     * - the first token of the line becomes the command (method),
     * - all remaining tokens become parameters (params),
     * - provides getters to access the method and parameters.
     */

    public FormatRequest(String input) {
        String[] tokens = input.trim().split(" "); // remove leading and trailing spaces.
        this.method = tokens[0]; // first token = method
        /*
        tokens[0] = "login"
        tokens[1] = "john"
        tokens[2] = "12345"
        */
        this.params = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, this.params, 0, tokens.length - 1);
    }

    public String getMethod() {
        return method;
    }

    public String[] getParams() {
        return params;
    }
}