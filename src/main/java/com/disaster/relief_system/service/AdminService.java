package com.disaster.relief_system.service;

import com.disaster.relief_system.entity.Admin;
import com.disaster.relief_system.entity.User;

import java.util.List;

public interface AdminService {

    Admin createAdmin(Admin admin);

    long getAdminCount();

    List<User> getAllUsers();

    void approveRequest(Long requestId);

    void rejectRequest(Long requestId);
}