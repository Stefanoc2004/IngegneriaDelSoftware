package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.AttributeConverter;

public class PermissionsArrayConverter implements AttributeConverter<UserRole[], String> {


    @Override
    public String convertToDatabaseColumn(UserRole[] userRoles) {
        String[] strings = new String[userRoles.length];
        for (int i = 0; i < userRoles.length; i++) {
            strings[i] = userRoles[i].toString();
        }
        return String.join(",", strings);
    }

    @Override
    public UserRole[] convertToEntityAttribute(String s) {
        String[] strings = s.split(",");
        UserRole[] userRoles = new UserRole[strings.length];
        for (int i = 0; i < strings.length; i++) {
            userRoles[i]= UserRole.valueOf(strings[i]);
        }
        return userRoles;
    }
}
