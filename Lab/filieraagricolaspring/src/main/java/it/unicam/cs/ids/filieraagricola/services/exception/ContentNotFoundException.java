package it.unicam.cs.ids.filieraagricola.services.exception;

public class ContentNotFoundException extends Exception {
    public ContentNotFoundException() {
        super("Content not found");
    }
}
