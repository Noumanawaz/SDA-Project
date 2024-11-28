package com.example.demo.model;

import java.time.LocalDateTime;

public class RentalTransaction {
    private int transactionId;
    private int userId;
    private int bikeId;
    private int rentalHours;
    private double totalCost;
    private LocalDateTime rentalStartTime;
    private String status;

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public int getRentalHours() {
        return rentalHours;
    }

    public void setRentalHours(int rentalHours) {
        this.rentalHours = rentalHours;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public LocalDateTime getRentalStartTime() {
        return rentalStartTime;
    }

    public void setRentalStartTime(LocalDateTime rentalStartTime) {
        this.rentalStartTime = rentalStartTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
