package com.disaster.relief_system.service;

import com.disaster.relief_system.dto.RegistrationRequest;
import com.disaster.relief_system.entity.*;

import java.util.List;

public interface UserService {

    CampManager addCampManager(CampManager user);

    WarehouseManager addWarehouseManager(WarehouseManager user);

    LogisticsOfficer addLogisticsOfficer(LogisticsOfficer user);

    User registerUser(RegistrationRequest request);

    CampManager assignCampToManager(Long managerId, Long campId);

    List<User> getAllUsers();

    User getUserById(Long id);

    void deleteUser(Long id);
}