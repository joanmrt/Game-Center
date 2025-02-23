package com.example.gamecenter;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
