package it.unicam.cs.ids.filieraagricola.service.exception;

public class ContentNotFoundException extends Exception {
    public ContentNotFoundException() {
        super("Content not found");
    }
}
