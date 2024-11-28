package com.example.demo.Controller;

import com.example.demo.model.*;
import com.example.demo.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") 
public class AuthController {
    String role;
    String email;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RentalTransactionService rentalTransactionService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private BikeService bikeService;
    
    @Autowired
    private DamageReportService damageReportService ;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role is required");
        }
        if (user.getRole().equalsIgnoreCase("admin")) {
            String result = adminService.registerUserAsAdmin(user);
            if (result.equals("Admin registered successfully!")) {
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
            }
        } else if (user.getRole().equalsIgnoreCase("user")) {
            String result = userService.registerUser(user);
            if (result.equals("User registered successfully!")) {
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role specified");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        role = loginRequest.getRole(); // Role from the request body

        if (role.equalsIgnoreCase("admin")) {
            String adminLoginMessage = adminService.loginAdmin(email, password);
            if (adminLoginMessage.equals("Admin login successful!")) {
                return ResponseEntity.ok(adminLoginMessage); // Return success for admin
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(adminLoginMessage); // Return failure for admin
            }
        } else if (role.equalsIgnoreCase("user")) {
            String userLoginMessage = userService.loginUser(email, password);
            if (userLoginMessage.equals("Login successful!")) {
                return ResponseEntity.ok(userLoginMessage); // Return success for user
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userLoginMessage); // Return failure for user
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role specified");
        }
    }

    @PostMapping("/returnBike")
    public ResponseEntity<String> returnBike(@RequestParam(name = "TransactionID") int transactionId) {
        boolean isTransactionCompleted = rentalTransactionService.completeRentalTransaction(transactionId);

        if (isTransactionCompleted) {
            RentalTransaction transaction = rentalTransactionService.findTransactionById(transactionId);

            if (transaction != null) {
                bikeService.updateBikeAvailability(transaction.getBikeId(), true);
                return ResponseEntity.ok("Bike returned successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to return the bike. Transaction may already be completed.");
        }
    }

    @GetMapping("/bikes")
    public List<Bike> getAllBikes() {
        return bikeService.getAllBikes();
    }

    @GetMapping("/bikes/{id}")
    public ResponseEntity<Bike> getBikeById(@PathVariable("id") int id) {
        Bike bike = bikeService.findById(id);

        if (bike != null) {
            return ResponseEntity.ok(bike);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PostMapping("/admin/addBike")
    public ResponseEntity<String> addBike(@RequestBody Bike bike) {
        if (!"admin".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Only admins can add bikes.");
        }
        
        try {
            bikeService.saveBike(bike); // Save the bike using the BikeService
            return ResponseEntity.status(HttpStatus.CREATED).body("Bike added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add bike: " + e.getMessage());
        }
    }
    @DeleteMapping("/admin/removeBike/{bikeId}")
    public ResponseEntity<String> removeBike(@PathVariable("bikeId") int bikeId) {
        if (!"admin".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Only admins can remove bikes.");
        }
        
        try {
            boolean isRemoved = bikeService.deleteBikeById(bikeId); // Delete the bike using the BikeService
            if (isRemoved) {
                return ResponseEntity.ok("Bike removed successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bike not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove bike: " + e.getMessage());
        }
    }
    @PostMapping("/rentalTransaction")
    public ResponseEntity<String> createRentalTransaction(@RequestBody RentalTransaction rentalTransaction) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        User user = userService.findUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        rentalTransaction.setUserId(user.getId()); 

        boolean isCreated = rentalTransactionService.createRentalTransaction(rentalTransaction);

        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Rental transaction created successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create rental transaction. Bike may not be available.");
        }
    }

    @GetMapping("/activeRentals")
    public ResponseEntity<?> getActiveRentalsForLoggedInUser() {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in first");
        }

        User user = userService.findUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        List<RentalTransaction> activeRentals = rentalTransactionService.findActiveRentalsByUserId(user.getId());

        if (activeRentals.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active rentals found.");
        }

        return ResponseEntity.ok(activeRentals);
    }

    @GetMapping("/allActiveRentals")
    public ResponseEntity<?> getAllActiveRentals() {
        List<RentalTransaction> activeRentals = rentalTransactionService.getActiveRentals();

        if (activeRentals.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active rentals found.");
        }

        return ResponseEntity.ok(activeRentals);
    }

    @GetMapping("/checkLoginStatus")
    public ResponseEntity<?> checkLoginStatus() {
        if (email != null && !email.isEmpty()) {
            return ResponseEntity.ok(new LoginResponse(email, role));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");
        }
    }

    @PostMapping("/maintenance/add")
    public String addMaintenanceRecord(
            @RequestParam(name = "bikeID", required = true) int bikeID,
            @RequestParam(name = "description", required = true) String description) {
        maintenanceService.addMaintenanceRecord(bikeID, description);
        return "Maintenance record added successfully!";
    }

    // Fixing maintenance endpoint to use @PathVariable for bikeID
    @GetMapping("/maintenance/{bikeID}")
    public List<Maintenance> getMaintenanceRecordsByBikeID(@PathVariable("bikeID") int bikeID) {
        return maintenanceService.getMaintenanceRecordsByBikeID(bikeID);
    }

    // Fixing maintenance endpoint and fixing typo "/maintenece" to "/maintenance"
    @GetMapping("/maintenance/all")
    public List<Maintenance> getAllMaintenanceRecords() {
        return maintenanceService.getAllMaintenanceRecords();
    }
    
    @PostMapping("/damageReport")
    public ResponseEntity<String> submitDamageReport(@RequestBody DamageReport report) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in first.");
        }

        User user = userService.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        report.setUserId(user.getId());
        report.setStatus("Pending");
        damageReportService.createReport(report);

        return ResponseEntity.status(HttpStatus.CREATED).body("Damage report submitted successfully.");
    }

    @GetMapping("/damageReports")
    public List<DamageReport> getAllDamageReports() {
        return damageReportService.getAllReports();
    }

    @PutMapping("/damageReport/verify/{reportId}")
    public ResponseEntity<String> verifyDamageReport(@PathVariable("reportId") int reportId) {
        // Log to check if the method is being called and the reportId value
        System.out.println("Verifying damage report with ID: " + reportId);

        if (!"admin".equalsIgnoreCase(role)) {
            // Log for unauthorized access attempt
            System.out.println("Unauthorized access attempt");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can verify reports.");
        }

        try {
            damageReportService.verifyReport(reportId);
            return ResponseEntity.ok("Damage report verified successfully.");
        } catch (Exception e) {
            // Log the exception details to identify the problem
            System.err.println("Error occurred while verifying report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to verify the damage report.");
        }
    }


    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("Welcome to the API!");
    }

    @GetMapping("/home")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("This is the home page.");
    }
}

