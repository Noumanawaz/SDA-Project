package com.example.demo.Service;

import com.example.demo.model.Bike;
import com.example.demo.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeService {
    @Autowired
    private BikeRepository bikeRepository;

    public List<Bike> getAllBikes() {
        return bikeRepository.findAllBikes();
    }
    public Bike findById(int bikeID) {
        return bikeRepository.findById(bikeID); // Fetch bike by ID
    }
    public void updateBikeAvailability(int bikeId, boolean availability) {
        bikeRepository.updateAvailability(bikeId, availability);
    }
    public boolean deleteBikeById(int bikeId) {
        return bikeRepository.deleteBikeById(bikeId);
    }
    // Save a new bike
    public void saveBike(Bike bike) {
        bikeRepository.saveBike(bike);
    }
}
