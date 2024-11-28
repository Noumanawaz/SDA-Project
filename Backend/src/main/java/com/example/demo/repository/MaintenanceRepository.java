package com.example.demo.repository;

import com.example.demo.model.Maintenance;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MaintenanceRepository {
    private final JdbcTemplate jdbcTemplate;

    public MaintenanceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addMaintenanceRecord(int bikeID, String description) {
        String sql = "INSERT INTO Maintenance (BikeID, Description) VALUES (?, ?)";
        jdbcTemplate.update(sql, bikeID, description);
    }

    public List<Maintenance> getMaintenanceRecordsByBikeID(int bikeID) {
        String sql = "SELECT * FROM Maintenance WHERE BikeID = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Maintenance.class), bikeID);
    }

    public List<Maintenance> getAllMaintenanceRecords() {
        String sql = "SELECT * FROM Maintenance";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Maintenance.class));
    }
}
