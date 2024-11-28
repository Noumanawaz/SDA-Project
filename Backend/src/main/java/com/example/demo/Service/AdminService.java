package com.example.demo.Service;

import com.example.demo.model.Admin;
import com.example.demo.model.User;
import com.example.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    // Register a new admin
    public String registerAdmin(Admin admin) {
        // Check if the email already exists as an admin
        if (adminRepository.findByEmail(admin.getEmail()) != null) {
            return "Admin with this email already exists!";
        }

        // Save the admin if the email doesn't exist
        adminRepository.saveAdmin(admin);
        return "Admin registered successfully!";
    }
    
    public String registerUserAsAdmin(User user) {
        // Create an Admin object from the User object
        Admin admin = new Admin();
        admin.setUsername(user.getUsername());
        admin.setEmail(user.getEmail());
        admin.setPassword(user.getPassword());
        admin.setPhoneNumber(user.getPhoneNumber());
        admin.setRole("admin"); // Set the role to 'admin'

        // Save the Admin object
        adminRepository.saveAdmin(admin);
        return "Admin registered successfully!";
    }

    // Admin login
    public String loginAdmin(String email, String password) {
        // Find the admin by email
        Admin admin = adminRepository.findByEmail(email);
        
        if (admin == null) {
            // Admin not found, return a login failure message
            return "Admin not found";
        }

        // Check if the password matches
        if (!admin.getPassword().equals(password)) {
            // Incorrect password, return a failure message
            return "Invalid password";
        }

        // Successful login
        return "Admin login successful!";
    }
}
