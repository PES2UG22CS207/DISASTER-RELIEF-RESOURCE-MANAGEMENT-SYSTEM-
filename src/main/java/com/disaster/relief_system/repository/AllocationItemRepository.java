package com.disaster.relief_system.repository;

import com.disaster.relief_system.entity.AllocationItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllocationItemRepository
        extends JpaRepository<AllocationItem, Long> {

        void deleteByAllocation_AllocationId(Long allocationId);
}