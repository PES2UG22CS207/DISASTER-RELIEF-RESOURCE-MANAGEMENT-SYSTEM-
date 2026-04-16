package com.disaster.relief_system.service.impl;

import com.disaster.relief_system.entity.Allocation;
import com.disaster.relief_system.entity.AllocationItem;
import com.disaster.relief_system.entity.InventoryItem;
import com.disaster.relief_system.entity.ReliefCamp;
import com.disaster.relief_system.entity.ResourceRequest;
import com.disaster.relief_system.entity.Warehouse;
import com.disaster.relief_system.enums.RequestStatus;
import com.disaster.relief_system.enums.ResourceCategory;
import com.disaster.relief_system.repository.AllocationItemRepository;
import com.disaster.relief_system.repository.AllocationRepository;
import com.disaster.relief_system.repository.InventoryRepository;
import com.disaster.relief_system.repository.RequestRepository;
import com.disaster.relief_system.service.AllocationService;
import com.disaster.relief_system.service.CampService;
import com.disaster.relief_system.service.WarehouseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AllocationServiceImpl implements AllocationService {

    private final AllocationRepository allocationRepository;
    private final AllocationItemRepository allocationItemRepository;
    private final RequestRepository requestRepository;
    private final WarehouseService warehouseService;
    private final CampService campService;
    private final InventoryRepository inventoryRepository;

    public AllocationServiceImpl(AllocationRepository allocationRepository,
                                 AllocationItemRepository allocationItemRepository,
                                 RequestRepository requestRepository,
                                 WarehouseService warehouseService,
                                 CampService campService,
                                 InventoryRepository inventoryRepository) {
        this.allocationRepository = allocationRepository;
        this.allocationItemRepository = allocationItemRepository;
        this.requestRepository = requestRepository;
        this.warehouseService = warehouseService;
        this.campService = campService;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Allocation createAllocation(Allocation allocation) {
        allocation.setAllocationDate(LocalDate.now());
        return allocationRepository.save(allocation);
    }

    @Override
    public List<Allocation> getAllAllocations() {
        return allocationRepository.findAll();
    }

    @Override
    public Allocation getAllocationById(Long id) {
        return allocationRepository.findById(id).orElse(null);
    }

    /**
     * Allocates from nearest warehouse by default.
     * If nearest stock is low and another warehouse can satisfy the request,
     * caller may opt-in to alternative allocation.
     */
    @Override
    @Transactional
    public void autoAllocate(Long requestId) {
        autoAllocate(requestId, false);
    }

    @Override
    @Transactional
    public void autoAllocate(Long requestId, boolean allowAlternativeWarehouse) {
        ResourceRequest request = requestRepository.findById(requestId).orElseThrow();

        if (request.getStatus() != RequestStatus.APPROVED) {
            throw new IllegalStateException("Request must be approved before allocation");
        }

        // Idempotent: don't create duplicate allocations for the same request.
        if (allocationRepository.findByRequest_RequestId(requestId).isPresent()) {
            return;
        }

        ReliefCamp camp = request.getReliefCamp();
        if (camp == null) {
            throw new IllegalStateException("Request is not linked to a camp");
        }

        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        if (warehouses.isEmpty()) {
            throw new IllegalStateException("No warehouses available");
        }

        ResourceCategory category = parseCategory(request.getResourceType());
        int requiredQty = request.getQuantity();

        Warehouse nearest = chooseNearestWarehouse(camp, warehouses);
        if (nearest == null) {
            throw new IllegalStateException("No warehouses available");
        }

        InventoryItem nearestInv = pickInventoryItemWithEnoughStock(nearest.getWarehouseId(), category, requiredQty);
        if (nearestInv != null && nearestInv.getResource() != null && nearestInv.getQuantity() >= requiredQty) {
            allocateRequestFromWarehouse(request, nearest, nearestInv, requiredQty);
            return;
        }

        Warehouse alternative = chooseAlternativeWarehouseWithStock(camp, warehouses, category, requiredQty);
        if (alternative == null) {
            throw new IllegalStateException("NO_STOCK_ANY|No request allocation because stock is not sufficient in any warehouse.");
        }

        String nearestName = nearest.getWarehouseName() != null ? nearest.getWarehouseName() : ("warehouseId=" + nearest.getWarehouseId());
        String altName = alternative.getWarehouseName() != null ? alternative.getWarehouseName() : ("warehouseId=" + alternative.getWarehouseId());
        if (!allowAlternativeWarehouse) {
            throw new IllegalStateException("LOW_STOCK_NEAREST|Nearest warehouse " + nearestName
                    + " has low stock. Allocate from " + altName + " instead?");
        }

        InventoryItem altInv = pickInventoryItemWithEnoughStock(alternative.getWarehouseId(), category, requiredQty);
        if (altInv == null || altInv.getResource() == null || altInv.getQuantity() < requiredQty) {
            throw new IllegalStateException("NO_STOCK_ANY|No request allocation because stock is not sufficient in any warehouse.");
        }

        allocateRequestFromWarehouse(request, alternative, altInv, requiredQty);
    }

    private void allocateRequestFromWarehouse(ResourceRequest request,
                                              Warehouse warehouse,
                                              InventoryItem inv,
                                              int requiredQty) {
        if (inv == null || inv.getResource() == null) {
            throw new IllegalStateException("No inventory found for requested resource");
        }
        if (inv.getQuantity() < requiredQty) {
            throw new IllegalStateException("Insufficient stock for requested resource");
        }

        Allocation allocation = new Allocation();
        allocation.setRequest(request);
        allocation.setWarehouse(warehouse);
        allocation.setAllocationDate(LocalDate.now());
        allocationRepository.save(allocation);

        AllocationItem item = new AllocationItem();
        item.setAllocation(allocation);
        item.setResource(inv.getResource());
        item.setQuantityAllocated(requiredQty);
        allocationItemRepository.save(item);

        inv.setQuantity(inv.getQuantity() - requiredQty);
        inventoryRepository.save(inv);

        request.setStatus(RequestStatus.ALLOCATED);
        requestRepository.save(request);
    }

    private Warehouse chooseNearestWarehouse(ReliefCamp camp, List<Warehouse> warehouses) {
        if (warehouses == null || warehouses.isEmpty()) {
            return null;
        }

        String campDistrict = camp.getDistrict();
        if (campDistrict != null && !campDistrict.isBlank()) {
            for (Warehouse w : warehouses) {
                if (w.getDistrict() != null && campDistrict.equalsIgnoreCase(w.getDistrict())) {
                    return w;
                }
            }
        }

        String campState = camp.getState();
        if (campState != null && !campState.isBlank()) {
            for (Warehouse w : warehouses) {
                if (w.getState() != null && campState.equalsIgnoreCase(w.getState())) {
                    return w;
                }
            }
        }

        return warehouses.get(0);
    }

    private Warehouse chooseNearestWarehouseForAllocation(ReliefCamp camp,
                                                          List<Warehouse> warehouses,
                                                          ResourceCategory category,
                                                          int requiredQty) {
        List<Warehouse> districtMatches = warehouses.stream()
                .filter(w -> sameText(camp.getDistrict(), w.getDistrict()))
                .toList();
        Warehouse inDistrict = firstWarehouseWithEnoughStock(districtMatches, category, requiredQty);
        if (inDistrict != null) {
            return inDistrict;
        }

        List<Warehouse> stateMatches = warehouses.stream()
                .filter(w -> sameText(camp.getState(), w.getState()))
                .toList();
        Warehouse inState = firstWarehouseWithEnoughStock(stateMatches, category, requiredQty);
        if (inState != null) {
            return inState;
        }

        return firstWarehouseWithEnoughStock(warehouses, category, requiredQty);
    }

    private Warehouse chooseAlternativeWarehouseWithStock(ReliefCamp camp,
                                                          List<Warehouse> warehouses,
                                                          ResourceCategory category,
                                                          int requiredQty) {
        Warehouse nearest = chooseNearestWarehouse(camp, warehouses);
        for (Warehouse warehouse : warehouses) {
            if (warehouse == null || warehouse.getWarehouseId() == null) {
                continue;
            }
            if (nearest != null && nearest.getWarehouseId() != null
                    && nearest.getWarehouseId().equals(warehouse.getWarehouseId())) {
                continue;
            }

            InventoryItem inv = pickInventoryItemWithEnoughStock(warehouse.getWarehouseId(), category, requiredQty);
            if (inv != null && inv.getResource() != null && inv.getQuantity() >= requiredQty) {
                return warehouse;
            }
        }
        return null;
    }

    private Warehouse firstWarehouseWithEnoughStock(List<Warehouse> warehouses,
                                                    ResourceCategory category,
                                                    int requiredQty) {
        if (warehouses == null || warehouses.isEmpty()) {
            return null;
        }

        for (Warehouse warehouse : warehouses) {
            InventoryItem inv = pickInventoryItemWithEnoughStock(warehouse.getWarehouseId(), category, requiredQty);
            if (inv != null && inv.getResource() != null && inv.getQuantity() >= requiredQty) {
                return warehouse;
            }
        }
        return null;
    }

    private InventoryItem pickInventoryItemWithEnoughStock(Long warehouseId,
                                                           ResourceCategory category,
                                                           int requiredQty) {
        List<InventoryItem> items = inventoryRepository
                .findByWarehouse_WarehouseIdAndResource_Category(warehouseId, category);

        InventoryItem best = null;
        for (InventoryItem item : items) {
            if (item == null || item.getResource() == null) {
                continue;
            }
            if (item.getQuantity() < requiredQty) {
                continue;
            }
            if (best == null || item.getQuantity() > best.getQuantity()) {
                best = item;
            }
        }
        return best;
    }

    private boolean sameText(String left, String right) {
        return left != null && right != null && left.trim().equalsIgnoreCase(right.trim());
    }

    private ResourceCategory parseCategory(String resourceType) {
        if (resourceType == null || resourceType.isBlank()) {
            throw new IllegalArgumentException("Resource type is required");
        }
        try {
            return ResourceCategory.valueOf(resourceType.trim().toUpperCase());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid resource type: " + resourceType);
        }
    }

    @Override
    public String getNearestWarehouse(Long campId) {
        ReliefCamp camp = campService.getCampById(campId);
        if (camp == null) {
            return "Camp not found";
        }

        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        if (warehouses.isEmpty()) {
            return "No warehouses available";
        }

        Warehouse nearest = chooseNearestWarehouse(camp, warehouses);
        if (nearest == null) {
            return "No warehouses available";
        }

        boolean sameDistrict = camp.getDistrict() != null && nearest.getDistrict() != null
                && camp.getDistrict().equalsIgnoreCase(nearest.getDistrict());
        boolean sameState = camp.getState() != null && nearest.getState() != null
                && camp.getState().equalsIgnoreCase(nearest.getState());

        String suffix = sameDistrict ? " (same district)" : sameState ? " (same state)" : " (nearest available)";
        return nearest.getWarehouseName() + suffix;
    }
}