package com.disaster.relief_system.service;

import com.disaster.relief_system.entity.Allocation;

import java.util.List;

public interface AllocationService {

    Allocation createAllocation(Allocation allocation);

    List<Allocation> getAllAllocations();

    Allocation getAllocationById(Long id);

    void autoAllocate(Long requestId);

    void autoAllocate(Long requestId, boolean allowAlternativeWarehouse);

    String getNearestWarehouse(Long campId);
}