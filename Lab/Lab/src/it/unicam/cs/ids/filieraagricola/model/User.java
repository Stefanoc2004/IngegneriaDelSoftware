package it.unicam.cs.ids.filieraagricola.model;

public class User implements Prototype {
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

    // Method clone for prototype
    @Override
    public User clone() {
        User user = new User(0, "", "", "");
        user.permissions = this.permissions;
        return user;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}