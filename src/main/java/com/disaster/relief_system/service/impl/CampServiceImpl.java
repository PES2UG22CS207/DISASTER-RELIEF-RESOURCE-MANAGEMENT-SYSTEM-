package com.disaster.relief_system.service.impl;

import com.disaster.relief_system.entity.Allocation;
import com.disaster.relief_system.entity.CampManager;
import com.disaster.relief_system.entity.ReliefCamp;
import com.disaster.relief_system.entity.ResourceRequest;
import com.disaster.relief_system.repository.AllocationItemRepository;
import com.disaster.relief_system.repository.AllocationRepository;
import com.disaster.relief_system.repository.CampManagerRepository;
import com.disaster.relief_system.repository.CampRepository;
import com.disaster.relief_system.repository.DeliveryRepository;
import com.disaster.relief_system.repository.RequestItemRepository;
import com.disaster.relief_system.repository.RequestRepository;
import com.disaster.relief_system.repository.UserRepository;
import com.disaster.relief_system.service.CampService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CampServiceImpl implements CampService {

    private final CampRepository repository;
    private final RequestRepository requestRepository;
    private final RequestItemRepository requestItemRepository;
    private final AllocationRepository allocationRepository;
    private final AllocationItemRepository allocationItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final CampManagerRepository campManagerRepository;
    private final UserRepository userRepository;

    public CampServiceImpl(CampRepository repository,
                           RequestRepository requestRepository,
                           RequestItemRepository requestItemRepository,
                           AllocationRepository allocationRepository,
                           AllocationItemRepository allocationItemRepository,
                           DeliveryRepository deliveryRepository,
                           CampManagerRepository campManagerRepository,
                           UserRepository userRepository) {
        this.repository = repository;
        this.requestRepository = requestRepository;
        this.requestItemRepository = requestItemRepository;
        this.allocationRepository = allocationRepository;
        this.allocationItemRepository = allocationItemRepository;
        this.deliveryRepository = deliveryRepository;
        this.campManagerRepository = campManagerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ReliefCamp addCamp(ReliefCamp camp) {
        return repository.save(camp);
    }

    @Override
    public List<ReliefCamp> getAllCamps() {
        return repository.findAll();
    }

    @Override
    public ReliefCamp getCampById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ReliefCamp getCampByManagerId(Long managerId) {
        return repository.findByCampManager_UserId(managerId);
    }

    @Override
    public ReliefCamp updateCamp(Long id, ReliefCamp camp) {

        camp.setCampId(id);

        return repository.save(camp);
    }

    @Override
    public void updatePopulation(Long id, int population) {

        ReliefCamp camp = repository.findById(id).orElseThrow();

        camp.setPopulation(population);

        repository.save(camp);
    }

    @Override
    public void updateSeverity(Long id, int severity) {

        ReliefCamp camp = repository.findById(id).orElseThrow();

        camp.setSeverityLevel(severity);

        repository.save(camp);
    }

    @Override
    @Transactional
    public void deleteCamp(Long id) {
        List<ResourceRequest> requests = requestRepository.findByReliefCamp_CampId(id);
        for (ResourceRequest request : requests) {
            allocationRepository.findByRequest_RequestId(request.getRequestId())
                    .ifPresent(this::deleteAllocationGraph);

            requestItemRepository.deleteByRequest_RequestId(request.getRequestId());
            requestRepository.deleteById(request.getRequestId());
        }

        List<CampManager> campManagers = campManagerRepository.findAllByReliefCamp_CampId(id);
        if (!campManagers.isEmpty()) {
            for (CampManager campManager : campManagers) {
                if (campManager.getUserId() != null) {
                    userRepository.deleteById(campManager.getUserId());
                }
            }
        }

        repository.deleteById(id);
    }

    private void deleteAllocationGraph(Allocation allocation) {
        deliveryRepository.deleteByAllocation_AllocationId(allocation.getAllocationId());
        allocationItemRepository.deleteByAllocation_AllocationId(allocation.getAllocationId());
        allocationRepository.deleteById(allocation.getAllocationId());
    }
}