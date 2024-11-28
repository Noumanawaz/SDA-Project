package com.example.demo.repository;

import com.example.demo.model.RentalTransaction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RentalTransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public RentalTransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save a new rental transaction
    public void saveRentalTransaction(RentalTransaction rentalTransaction) {
        String sql = "INSERT INTO RentalTransaction (UserID, BikeID, RentalHours, TotalCost, Status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, rentalTransaction.getUserId(), rentalTransaction.getBikeId(),
                rentalTransaction.getRentalHours(), rentalTransaction.getTotalCost(), "Active");
    }

    // Fetch all active rentals
    public List<RentalTransaction> findActiveRentals() {
        String sql = "SELECT * FROM RentalTransaction WHERE Status = 'Active'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RentalTransaction.class));
    }

    // Update the status of a rental transaction
    public boolean updateStatus(int transactionId, String status) {
        String sql = "UPDATE RentalTransaction SET Status = ? WHERE TransactionID = ?";
        return jdbcTemplate.update(sql, status, transactionId) > 0;
    }
    public RentalTransaction findById(int transactionId) {
        String sql = "SELECT * FROM RentalTransaction WHERE TransactionID = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(RentalTransaction.class), transactionId);
    }
 // Fetch all active rentals for a specific user
    public List<RentalTransaction> findActiveRentalsByUserId(int userId) {
        String sql = "SELECT * FROM RentalTransaction WHERE UserID = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RentalTransaction.class), userId);
    }

}