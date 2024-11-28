package com.example.demo.Service;

import com.example.demo.model.RentalTransaction;
import com.example.demo.repository.RentalTransactionRepository;
import com.example.demo.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalTransactionService {

    @Autowired
    private RentalTransactionRepository rentalTransactionRepository;

    @Autowired
    private BikeRepository bikeRepository;

    // Method to create a rental transaction
    public boolean createRentalTransaction(RentalTransaction rentalTransaction) {
        System.out.println("Bike ID IS : " + rentalTransaction.getBikeId());
        System.out.println("User ID IS : " + rentalTransaction.getUserId());

        if (!bikeRepository.bikeExists(rentalTransaction.getBikeId())) {
            System.out.println("Bike ni hai");
            return false; // Bike does not exist
        }

        boolean isBikeAvailable = bikeRepository.checkAvailability(rentalTransaction.getBikeId());
        if (!isBikeAvailable) {
            System.out.println("Bike ni available");
            return false; // Bike is not available
        }

        double pricePerHour = bikeRepository.getBikePricePerHour(rentalTransaction.getBikeId());
        rentalTransaction.setTotalCost(rentalTransaction.getRentalHours() * pricePerHour);

        rentalTransactionRepository.saveRentalTransaction(rentalTransaction);
        bikeRepository.updateAvailability(rentalTransaction.getBikeId(), false);

        return true;
    }

    // Method to fetch all active rentals
    public List<RentalTransaction> getActiveRentals() {
        return rentalTransactionRepository.findActiveRentals();
    }

    // Method to update transaction status
    public boolean completeRentalTransaction(int transactionId) {
        return rentalTransactionRepository.updateStatus(transactionId, "Completed");
    }

    // Method to fetch a rental transaction by ID
    public RentalTransaction findTransactionById(int transactionId) {
        return rentalTransactionRepository.findById(transactionId);
    }

    // Method to fetch all active rentals for a specific user
    public List<RentalTransaction> findActiveRentalsByUserId(int userId) {
        return rentalTransactionRepository.findActiveRentalsByUserId(userId);
    }
    
}
