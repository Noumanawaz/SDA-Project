package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    // Constructor injection of JdbcTemplate
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Find user by email
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try {
            // Querying for a single object (expecting one result or none)
            return jdbcTemplate.queryForObject(
                    sql, 
                    new Object[]{email}, 
                    new BeanPropertyRowMapper<>(User.class)
            );
        } catch (EmptyResultDataAccessException e) {
            // If no user is found, return null
            return null;
        }
    }

    public void saveUser(User user) {
    	System.out.print(user.getPhoneNumber());
        String sql = "INSERT INTO users (username, email, password, phone, role) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.getRole());
    }



    // Method to fetch all users (example)
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(
                sql, 
                new BeanPropertyRowMapper<>(User.class)
        );
    }
}
