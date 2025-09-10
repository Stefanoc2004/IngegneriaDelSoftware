package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.AttributeConverter;

public class PermissionsArrayConverter implements AttributeConverter<String[], String> {


    @Override
    public String convertToDatabaseColumn(String[] strings) {
        return String.join(",",strings);
    }

    @Override
    public String[] convertToEntityAttribute(String s) {
        return s.split(",");
    }
}
