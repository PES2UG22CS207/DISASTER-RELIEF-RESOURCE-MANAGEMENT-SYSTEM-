package com.disaster.relief_system.service.impl;

import com.disaster.relief_system.entity.ReliefCamp;
import com.disaster.relief_system.entity.ResourceRequest;
import com.disaster.relief_system.enums.RequestStatus;
import com.disaster.relief_system.repository.RequestRepository;
import com.disaster.relief_system.service.AllocationService;
import com.disaster.relief_system.service.CampService;
import com.disaster.relief_system.service.RequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository repository;
    private final CampService campService;
    private final AllocationService allocationService;

    public RequestServiceImpl(RequestRepository repository, CampService campService, AllocationService allocationService) {
        this.repository = repository;
        this.campService = campService;
        this.allocationService = allocationService;
    }

    @Override
    public List<ResourceRequest> getAllRequests() {
        return repository.findAll();
    }

    @Override
    public List<ResourceRequest> getRequestsByManager(Long managerId) {
        ReliefCamp camp = campService.getCampByManagerId(managerId);
        if (camp == null) return List.of();
        return getRequestsByCampId(camp.getCampId());
    }

    @Override
    public ResourceRequest createRequest(ResourceRequest request) {
        if (request.getReliefCamp() == null || request.getReliefCamp().getCampId() == null) {
            throw new IllegalArgumentException("Camp is required");
        }
        if (request.getResourceType() == null || request.getResourceType().isBlank()) {
            throw new IllegalArgumentException("Resource type is required");
        }
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }
        request.setStatus(RequestStatus.PENDING);
        request.setRequestDate(LocalDate.now());
        return repository.save(request);
    }

    @Override
    @Transactional
    public void approveRequest(Long id) {
        approveRequest(id, false);
    }

    @Override
    @Transactional
    public void approveRequest(Long id, boolean allowAlternativeWarehouse) {
        ResourceRequest request = repository.findById(id).orElseThrow();

        if (request.getStatus() == RequestStatus.ALLOCATED || request.getStatus() == RequestStatus.DELIVERED) {
            return;
        }
        if (request.getStatus() == RequestStatus.REJECTED) {
            throw new IllegalStateException("Rejected request cannot be approved");
        }

        request.setStatus(RequestStatus.APPROVED);
        repository.save(request);

        // On approval: allocate from nearest warehouse, reduce inventory, and move status to ALLOCATED.
        try {
            allocationService.autoAllocate(id, allowAlternativeWarehouse);
        } catch (Exception ex) {
            request.setStatus(RequestStatus.PENDING);
            repository.save(request);
            if (ex instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public void rejectRequest(Long id) {
        ResourceRequest request = repository.findById(id).orElseThrow();
        request.setStatus(RequestStatus.REJECTED);
        repository.save(request);
    }

    @Override
    public void autoAllocateRequest(Long id) {
        allocationService.autoAllocate(id);
    }

    @Override
    public void markDelivered(Long id) {
        ResourceRequest request = repository.findById(id).orElseThrow();
        request.setStatus(RequestStatus.DELIVERED);
        repository.save(request);
    }

    @Override
    public ResourceRequest getRequestById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<ResourceRequest> getRequestsByCampId(Long campId) {
        return repository.findAll()
                .stream()
                .filter(r -> r.getReliefCamp().getCampId().equals(campId))
                .collect(Collectors.toList());
    }
}