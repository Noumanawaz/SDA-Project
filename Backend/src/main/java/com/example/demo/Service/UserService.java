package com.example.demo.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register a new user
    public String registerUser(User user) {
        // Check if the email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "User with this email already exists!";
        }

        // Save the user if the email doesn't exist
        userRepository.saveUser(user);
        return "User registered successfully!";
    }

    public String loginUser(String email, String password) {
        // Find the user by email
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            // User not found, return a login failure message
            return "User not found";
        }

        // Check if the password matches
        if (!user.getPassword().equals(password)) {
            // Incorrect password, return a failure message
            return "Invalid password";
        }

        // Successful login
        return "Login successful!";
    }
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);  // Retrieve user by email
    }
    
}