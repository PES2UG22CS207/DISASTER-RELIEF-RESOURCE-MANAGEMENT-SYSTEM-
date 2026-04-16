package com.disaster.relief_system.controller;

import com.disaster.relief_system.entity.CampManager;
import com.disaster.relief_system.entity.User;
import com.disaster.relief_system.entity.WarehouseManager;
import com.disaster.relief_system.repository.UserRepository;
import com.disaster.relief_system.entity.User;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

import java.util.HashMap;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get Current User (safe fields only)
    @GetMapping("/current-user")
    public Map<String, Object> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }

        Map<String, Object> dto = new HashMap<>();
        dto.put("userId", user.getUserId());
        dto.put("name", user.getName());
        dto.put("email", user.getEmail());
        dto.put("role", user.getRole());

        if (user instanceof CampManager campManager && campManager.getReliefCamp() != null) {
            dto.put("campId", campManager.getReliefCamp().getCampId());
        }
        if (user instanceof WarehouseManager warehouseManager && warehouseManager.getWarehouse() != null) {
            dto.put("warehouseId", warehouseManager.getWarehouse().getWarehouseId());
        }

        return dto;
    }
}