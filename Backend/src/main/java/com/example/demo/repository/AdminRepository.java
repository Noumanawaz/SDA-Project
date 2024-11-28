package com.example.demo.repository;

import com.example.demo.model.Admin;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

@Repository
public class AdminRepository {

    private final JdbcTemplate jdbcTemplate;

    // Constructor injection of JdbcTemplate
    public AdminRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Find admin by email
    public Admin findByEmail(String email) {
        String sql = "SELECT * FROM admin WHERE email = ? AND role = 'admin'";

        try {
            // Querying for a single admin object
            return jdbcTemplate.queryForObject(
                    sql, 
                    new Object[]{email}, 
                    new BeanPropertyRowMapper<>(Admin.class)
            );
        } catch (EmptyResultDataAccessException e) {
            // If no admin is found, return null
            return null;
        }
    }

    // Save admin to database
    public void saveAdmin(Admin admin) {
    	System.out.print(admin.getUsername());
        String sql = "INSERT INTO admin (username, email, password, phone, role) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, admin.getUsername(), admin.getEmail(), admin.getPassword(), admin.getPhoneNumber(), admin.getRole());
    }
    
    
    // Method to fetch all admins (example)
    public List<Admin> findAllAdmins() {
        String sql = "SELECT * FROM admin WHERE role = 'admin'";
        return jdbcTemplate.query(
                sql, 
                new BeanPropertyRowMapper<>(Admin.class)
        );
    }
}
