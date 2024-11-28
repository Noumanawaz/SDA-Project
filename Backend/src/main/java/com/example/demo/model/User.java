package com.example.demo.model;

public class User extends Person {
    private String role;

    // No-argument constructor (this is necessary for Spring to instantiate the object)
    public User() {
        this.role = "user";  // Setting the default role as "user"
    }

    // Getter for role
    public String getRole() {
        return role;
    }

    // Setter for role (if you ever want to change it)
    public void setRole(String role) {
        this.role = role;
    }
    
}
