package com.disaster.relief_system.service.impl;

import com.disaster.relief_system.dto.RegistrationRequest;
import com.disaster.relief_system.entity.*;
import com.disaster.relief_system.enums.Role;
import com.disaster.relief_system.repository.UserRepository;
import com.disaster.relief_system.service.CampService;
import com.disaster.relief_system.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final CampService campService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, CampService campService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.campService = campService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CampManager addCampManager(CampManager user) {
        user.setRole(Role.CAMP_MANAGER);
        return (CampManager) repository.save(user);
    }

    @Override
    public WarehouseManager addWarehouseManager(WarehouseManager user) {
        user.setRole(Role.WAREHOUSE_MANAGER);
        return (WarehouseManager) repository.save(user);
    }

    @Override
    public LogisticsOfficer addLogisticsOfficer(LogisticsOfficer user) {
        user.setRole(Role.LOGISTICS_OFFICER);
        return (LogisticsOfficer) repository.save(user);
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        if (repository.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user;
        switch (request.getRole()) {
            case ADMIN:
                if (repository.countByRole(Role.ADMIN) >= 1) {
                    throw new IllegalArgumentException("Only one admin account may be registered");
                }
                user = new Admin();
                break;
            case CAMP_MANAGER:
                user = new CampManager();
                break;
            case WAREHOUSE_MANAGER:
                user = new WarehouseManager();
                break;
            case LOGISTICS_OFFICER:
                user = new LogisticsOfficer();
                break;
            default:
                throw new IllegalArgumentException("Registration for this role is not allowed");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return repository.save(user);
    }

    @Override
    public CampManager assignCampToManager(Long managerId, Long campId) {
        User user = repository.findById(managerId).orElseThrow();
        if (!(user instanceof CampManager campManager)) {
            throw new IllegalArgumentException("User is not a Camp Manager");
        }

        var camp = campService.getCampById(campId);
        if (camp == null) {
            throw new IllegalArgumentException("Camp not found");
        }

        campManager.setReliefCamp(camp);
        return (CampManager) repository.save(campManager);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}