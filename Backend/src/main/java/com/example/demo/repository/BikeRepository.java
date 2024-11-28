package com.example.demo.repository;

import com.example.demo.model.Bike;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BikeRepository {
    private final JdbcTemplate jdbcTemplate;

    public BikeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Bike> findAllBikes() {
        String sql = "SELECT * FROM Bike";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Bike.class));
    }
    public boolean checkAvailability(int bikeId) {
        String sql = "SELECT Availability FROM Bike WHERE BikeID = ?";
        List<Boolean> results = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getBoolean("Availability"), bikeId);

        return !results.isEmpty() && results.get(0);
    }
    public boolean bikeExists(int bikeId) {
        String sql = "SELECT COUNT(*) FROM Bike WHERE BikeID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, bikeId);
        return count != null && count > 0;
    }


    // Get the price per hour of a bike
    public double getBikePricePerHour(int bikeId) {
        String sql = "SELECT PricePerHour FROM Bike WHERE BikeID = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, bikeId);
    }

    // Update bike availability
    public void updateAvailability(int bikeId, boolean availability) {
        String sql = "UPDATE Bike SET Availability = ? WHERE BikeID = ?";
        jdbcTemplate.update(sql, availability, bikeId);
    }
    public Bike findById(int bikeID) {
        String sql = "SELECT BikeID, model, brand, color, descr, type, year, pictureURLs, availability, pricePerHour, location FROM Bike WHERE BikeID = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Bike.class), bikeID);
    }
    
    public void saveBike(Bike bike) {
        // Hardcode default value for PictureURLs as an empty JSON array
        String sql = "INSERT INTO Bike (Model, Brand, Color, Descr, Type, Year, PictureURLs, Availability, PricePerHour, location) " +
                     "VALUES (?, ?, ?, ?, ?, ?, JSON_ARRAY(), ?, ?, ?)";

        // Insert data, using an empty JSON array for PictureURLs
        jdbcTemplate.update(sql, bike.getModel(), bike.getBrand(), bike.getColor(), bike.getDescr(),
                bike.getType(), bike.getYear(), bike.isAvailability(), bike.getPricePerHour(), bike.getLocation());
    }


    // Delete a bike by ID
    public boolean deleteBikeById(int bikeId) {
        String sql = "DELETE FROM Bike WHERE BikeID = ?";
        return jdbcTemplate.update(sql, bikeId) > 0;
    }

    // Find a bike by ID
    public Bike findBikeById(int bikeId) {
        String sql = "SELECT * FROM Bike WHERE BikeID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{bikeId}, new BeanPropertyRowMapper<>(Bike.class));
        } catch (Exception e) {
            return null;
        }
    }

    // Update bike availability
    public void updateBikeAvailability(int bikeId, boolean availability) {
        String sql = "UPDATE Bike SET Availability = ? WHERE BikeID = ?";
        jdbcTemplate.update(sql, availability ? 1 : 0, bikeId);
    }

}