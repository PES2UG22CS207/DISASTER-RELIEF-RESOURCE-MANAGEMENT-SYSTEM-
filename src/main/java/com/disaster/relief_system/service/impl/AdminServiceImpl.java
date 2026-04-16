package com.disaster.relief_system.service.impl;

import com.disaster.relief_system.entity.Admin;
import com.disaster.relief_system.entity.ResourceRequest;
import com.disaster.relief_system.entity.User;
import com.disaster.relief_system.enums.RequestStatus;
import com.disaster.relief_system.repository.AdminRepository;
import com.disaster.relief_system.repository.RequestRepository;
import com.disaster.relief_system.repository.UserRepository;
import com.disaster.relief_system.service.AdminService;
import com.disaster.relief_system.service.AllocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final AllocationService allocationService;

    public AdminServiceImpl(AdminRepository adminRepository,
                            UserRepository userRepository,
                            RequestRepository requestRepository,
                            AllocationService allocationService) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.allocationService = allocationService;
    }

    @Override
    public Admin createAdmin(Admin admin) {

        if (adminRepository.count() >= 1) {
            throw new RuntimeException("Only one admin allowed");
        }

        return adminRepository.save(admin);
    }

    @Override
    public long getAdminCount() {
        return adminRepository.count();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void approveRequest(Long requestId) {

        ResourceRequest request = requestRepository.findById(requestId).orElseThrow(
                () -> new IllegalArgumentException("Request not found with ID: " + requestId)
        );

        if (request.getStatus() == RequestStatus.ALLOCATED || request.getStatus() == RequestStatus.DELIVERED) {
            throw new IllegalStateException("Request is already " + request.getStatus() + " and cannot be re-approved");
        }
        if (request.getStatus() == RequestStatus.REJECTED) {
            throw new IllegalStateException("Rejected request cannot be approved");
        }

        request.setStatus(RequestStatus.APPROVED);
        requestRepository.save(request);

        // On approval: allocate from nearest warehouse, reduce inventory, and move status to ALLOCATED.
        try {
            allocationService.autoAllocate(requestId);
        } catch (Exception ex) {
            // If allocation fails, reset request status
            request.setStatus(RequestStatus.PENDING);
            requestRepository.save(request);
            throw new RuntimeException("Approval failed during allocation: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void rejectRequest(Long requestId) {

        ResourceRequest request =
                requestRepository.findById(requestId).orElseThrow();

        request.setStatus(RequestStatus.REJECTED);

        requestRepository.save(request);
    }
}