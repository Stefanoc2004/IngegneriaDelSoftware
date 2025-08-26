package it.unicam.cs.ids.filieraagricola.system;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private String[]  permissions;

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public User(int id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getName() {
        return name;
    }
}