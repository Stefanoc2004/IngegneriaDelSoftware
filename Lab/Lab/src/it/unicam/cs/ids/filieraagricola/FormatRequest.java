package it.unicam.cs.ids.filieraagricola;

public class FormatRequest {
    private String method;
    private String[] params;

    /**
     * Constructor that takes a raw input line.
     * Example: "login john 12345"
     */
    public FormatRequest(String input) {
        String[] tokens = input.trim().split(" ");
        this.method = tokens[0]; // first token = method
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