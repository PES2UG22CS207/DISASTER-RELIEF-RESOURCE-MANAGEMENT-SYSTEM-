package com.disaster.relief_system.controller;

import com.disaster.relief_system.dto.RegistrationRequest;
import com.disaster.relief_system.entity.*;
import com.disaster.relief_system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // 0. Register User
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        try {
            User created = service.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // 1. Add Camp Manager
    @PostMapping("/camp-manager")
    public CampManager addCampManager(@RequestBody CampManager user) {
        return service.addCampManager(user);
    }

    // 2. Add Logistics Officer
    @PostMapping("/logistics")
    public LogisticsOfficer addLogistics(@RequestBody LogisticsOfficer user) {
        return service.addLogisticsOfficer(user);
    }

    // 3. Add Warehouse Manager
    @PostMapping("/warehouse-manager")
    public WarehouseManager addWarehouseManager(@RequestBody WarehouseManager user) {
        return service.addWarehouseManager(user);
    }

    // 4. Get All Users
    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    // 5. Get User By ID
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    // 6. Link a Camp to the Camp Manager
    @PutMapping("/camp-manager/{managerId}/camp/{campId}")
    public CampManager assignCampToManager(@PathVariable Long managerId,
                                          @PathVariable Long campId) {
        return service.assignCampToManager(managerId, campId);
    }

    // 7. Remove User
    @DeleteMapping("/{id}")
    public String remove(@PathVariable Long id) {
        service.deleteUser(id);
        return "User Removed Successfully";
    }
}