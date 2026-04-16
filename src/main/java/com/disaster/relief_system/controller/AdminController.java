package com.disaster.relief_system.controller;

import com.disaster.relief_system.entity.Admin;
import com.disaster.relief_system.entity.User;
import com.disaster.relief_system.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    // Create Super Admin
    @PostMapping("/create")
    public Admin createAdmin(@RequestBody Admin admin) {
        return service.createAdmin(admin);
    }

    // Count Admin
    @GetMapping("/count")
    public long count() {
        return service.getAdminCount();
    }

    // Dashboard
    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {

        Map<String, Object> map = new HashMap<>();
        map.put("message", "Admin Dashboard");
        map.put("adminCount", service.getAdminCount());

        return map;
    }

    // Get All Users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    // Approve Request
    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        try {
            service.approveRequest(id);
            return ResponseEntity.ok("Request approved and allocated successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // Reject Request
    @PostMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        try {
            service.rejectRequest(id);
            return ResponseEntity.ok("Request rejected successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}