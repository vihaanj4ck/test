package model;

public class User {
    private final String userId;
    private final String name;
    private final String contact;
    private final String location;

    // Constructor
    public User(String userId, String name, String contact, String location) {
        this.userId = userId;
        this.name = name;
        this.contact = contact;
        this.location = location;
    }

    // Proper getters with return statements
    public String getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public String getContact() {
        return this.contact;
    }

    public String getLocation() {
        return this.location;
    }
}