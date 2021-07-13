package com.example.miniproject;

public class UserData {
    String username, email, id;

    public UserData() {
    }

    public UserData(String username, String email, String id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}
