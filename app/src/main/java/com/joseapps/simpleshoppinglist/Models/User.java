package com.joseapps.simpleshoppinglist.Models;

public class User {
    private String user_id;
    private String email;
    private String username;
    private String parent_name;

    public User(String user_id, String email, String username, String parent_name) {
        this.user_id = user_id;
        this.email = email;
        this.username = username;
        this.parent_name = parent_name;
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

    public String getParent_name() {
        return parent_name;
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

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", parent_name='" + parent_name + '\'' +
                '}';
    }
}
