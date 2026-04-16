package com.disaster.relief_system.service.impl;

import com.disaster.relief_system.entity.Delivery;
import com.disaster.relief_system.entity.ResourceRequest;
import com.disaster.relief_system.enums.DeliveryStatus;
import com.disaster.relief_system.enums.RequestStatus;
import com.disaster.relief_system.repository.AllocationRepository;
import com.disaster.relief_system.repository.DeliveryRepository;
import com.disaster.relief_system.repository.RequestRepository;
import com.disaster.relief_system.service.DeliveryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository repository;
    private final RequestRepository requestRepository;
    private final AllocationRepository allocationRepository;

    public DeliveryServiceImpl(DeliveryRepository repository,
                               RequestRepository requestRepository,
                               AllocationRepository allocationRepository) {
        this.repository = repository;
        this.requestRepository = requestRepository;
        this.allocationRepository = allocationRepository;
    }

    @Override
    public Delivery createDelivery(Delivery delivery) {
        delivery.setStatus(DeliveryStatus.PREPARING);
        delivery.setDispatchDate(LocalDate.now());

        if (delivery.getAllocation() == null || delivery.getAllocation().getAllocationId() == null) {
            throw new IllegalStateException("Allocation is required");
        }

        var allocation = allocationRepository.findById(delivery.getAllocation().getAllocationId())
                .orElseThrow(() -> new IllegalStateException("Allocation not found"));
        delivery.setAllocation(allocation);

        // Prevent duplicate delivery creation for same request
        if (allocation.getRequest() != null && allocation.getRequest().getRequestId() != null) {
            if (repository.findByAllocation_Request_RequestId(allocation.getRequest().getRequestId()).isPresent()) {
                throw new IllegalStateException("Delivery already exists for this request");
            }
        }

        // Destination defaults from camp district
        if (delivery.getDestinationDistrict() == null && allocation.getRequest() != null) {
            ResourceRequest request = requestRepository.findById(allocation.getRequest().getRequestId()).orElse(null);
            if (request != null && request.getReliefCamp() != null) {
                delivery.setDestinationDistrict(request.getReliefCamp().getDistrict());
            }
        }

        // Inventory is reduced at allocation time (approval), not at delivery creation.
        return repository.save(delivery);
    }

    @Override
    public List<Delivery> getAllDeliveries() {
        return repository.findAll();
    }

    @Override
    public Delivery getDeliveryById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void markPreparing(Long id) {
        Delivery delivery = repository.findById(id).orElseThrow();
        delivery.setStatus(DeliveryStatus.PREPARING);
        repository.save(delivery);
    }

    @Override
    public void markInTransit(Long id) {
        Delivery delivery = repository.findById(id).orElseThrow();
        delivery.setStatus(DeliveryStatus.IN_TRANSIT);
        repository.save(delivery);
    }

    @Override
    public void markDelivered(Long id) {
        Delivery delivery = repository.findById(id).orElseThrow();
        delivery.setStatus(DeliveryStatus.DELIVERED);
        delivery.setDeliveredDate(LocalDate.now());
        repository.save(delivery);

        if (delivery.getAllocation() != null && delivery.getAllocation().getRequest() != null) {
            ResourceRequest request = requestRepository.findById(delivery.getAllocation().getRequest().getRequestId()).orElse(null);
            if (request != null) {
                request.setStatus(RequestStatus.DELIVERED);
                requestRepository.save(request);
            }
        }
    }

    @Override
    public List<Delivery> getPendingDeliveries() {
        return repository.findAll()
                .stream()
                .filter(d -> d.getStatus() != DeliveryStatus.DELIVERED)
                .collect(Collectors.toList());
    }

    @Override
    public List<Delivery> getDeliveredDeliveries() {
        return repository.findAll()
                .stream()
                .filter(d -> d.getStatus() == DeliveryStatus.DELIVERED)
                .collect(Collectors.toList());
    }
}