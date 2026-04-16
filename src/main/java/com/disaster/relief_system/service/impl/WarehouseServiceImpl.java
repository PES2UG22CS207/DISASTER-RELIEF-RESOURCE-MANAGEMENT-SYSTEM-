package com.disaster.relief_system.service.impl;

import com.disaster.relief_system.entity.Allocation;
import com.disaster.relief_system.entity.ResourceRequest;
import com.disaster.relief_system.entity.Warehouse;
import com.disaster.relief_system.entity.WarehouseManager;
import com.disaster.relief_system.enums.RequestStatus;
import com.disaster.relief_system.repository.AllocationItemRepository;
import com.disaster.relief_system.repository.AllocationRepository;
import com.disaster.relief_system.repository.DeliveryRepository;
import com.disaster.relief_system.repository.InventoryRepository;
import com.disaster.relief_system.repository.RequestRepository;
import com.disaster.relief_system.repository.WarehouseRepository;
import com.disaster.relief_system.repository.WarehouseManagerRepository;
import com.disaster.relief_system.service.WarehouseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository repository;
    private final WarehouseManagerRepository warehouseManagerRepository;
    private final AllocationRepository allocationRepository;
    private final AllocationItemRepository allocationItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final InventoryRepository inventoryRepository;
    private final RequestRepository requestRepository;

    public WarehouseServiceImpl(WarehouseRepository repository,
                                WarehouseManagerRepository warehouseManagerRepository,
                                AllocationRepository allocationRepository,
                                AllocationItemRepository allocationItemRepository,
                                DeliveryRepository deliveryRepository,
                                InventoryRepository inventoryRepository,
                                RequestRepository requestRepository) {
        this.repository = repository;
        this.warehouseManagerRepository = warehouseManagerRepository;
        this.allocationRepository = allocationRepository;
        this.allocationItemRepository = allocationItemRepository;
        this.deliveryRepository = deliveryRepository;
        this.inventoryRepository = inventoryRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        return repository.findAll();
    }

    @Override
    public Warehouse getWarehouseById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Warehouse addWarehouse(Warehouse warehouse) {

        // Max 10 warehouses allowed
        if (repository.count() >= 10) {
            throw new RuntimeException("Maximum 10 warehouses allowed");
        }

        return repository.save(warehouse);
    }

    @Override
    public Warehouse updateWarehouse(Long id, Warehouse warehouse) {

        warehouse.setWarehouseId(id);

        return repository.save(warehouse);
    }

    @Override
    @Transactional
    public void deleteWarehouse(Long id) {
        List<WarehouseManager> managers = warehouseManagerRepository.findAllByWarehouse_WarehouseId(id);
        for (WarehouseManager manager : managers) {
            manager.setWarehouse(null);
        }
        if (!managers.isEmpty()) {
            warehouseManagerRepository.saveAll(managers);
        }

        List<Allocation> allocations = allocationRepository.findAllByWarehouse_WarehouseId(id);
        for (Allocation allocation : allocations) {
            deliveryRepository.deleteByAllocation_AllocationId(allocation.getAllocationId());
            allocationItemRepository.deleteByAllocation_AllocationId(allocation.getAllocationId());

            ResourceRequest request = allocation.getRequest();
            if (request != null && request.getStatus() == RequestStatus.ALLOCATED) {
                request.setStatus(RequestStatus.APPROVED);
                requestRepository.save(request);
            }

            allocationRepository.deleteById(allocation.getAllocationId());
        }

        inventoryRepository.deleteByWarehouse_WarehouseId(id);
        repository.deleteById(id);
    }
}