package com.example.demo.Service;

import com.example.demo.model.Maintenance;
import com.example.demo.repository.MaintenanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    public void addMaintenanceRecord(int bikeID, String description) {
        maintenanceRepository.addMaintenanceRecord(bikeID, description);
    }

    public List<Maintenance> getMaintenanceRecordsByBikeID(int bikeID) {
        return maintenanceRepository.getMaintenanceRecordsByBikeID(bikeID);
    }

    public List<Maintenance> getAllMaintenanceRecords() {
        return maintenanceRepository.getAllMaintenanceRecords();
    }
    
}
