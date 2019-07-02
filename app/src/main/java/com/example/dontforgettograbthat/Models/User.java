package com.example.dontforgettograbthat.Models;

public class User {
    private String user_id;
    private String email;
    private String username;
    private String family_name;

    public User(String user_id, String email, String username, String family_name) {
        this.user_id = user_id;
        this.email = email;
        this.username = username;
        this.family_name = family_name;
    }

    public User(){

    }

    public String getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", family_name='" + family_name + '\'' +
                '}';
    }
}
