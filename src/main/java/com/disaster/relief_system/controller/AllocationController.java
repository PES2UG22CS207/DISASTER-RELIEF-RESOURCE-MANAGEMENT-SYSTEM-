package com.disaster.relief_system.controller;

import com.disaster.relief_system.entity.Allocation;
import com.disaster.relief_system.service.AllocationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/allocations")
public class AllocationController {

    private final AllocationService service;

    public AllocationController(AllocationService service) {
        this.service = service;
    }

    // 1. Create Allocation
    @PostMapping
    public Allocation create(@RequestBody Allocation allocation) {
        return service.createAllocation(allocation);
    }

    // 2. Get All Allocations
    @GetMapping
    public List<Allocation> getAll() {
        return service.getAllAllocations();
    }

    // 3. Get Allocation By ID
    @GetMapping("/{id}")
    public Allocation getById(@PathVariable Long id) {
        return service.getAllocationById(id);
    }

    // 4. Auto Allocate By Request ID
    @PostMapping("/auto/{requestId}")
    public String autoAllocate(@PathVariable Long requestId) {
        service.autoAllocate(requestId);
        return "Auto Allocation Completed";
    }

    // 5. Get Nearest Warehouse
    @GetMapping("/nearest/{campId}")
    public String nearest(@PathVariable Long campId) {
        return service.getNearestWarehouse(campId);
    }
}