package com.disaster.relief_system.repository;

import com.disaster.relief_system.entity.Allocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {

	Optional<Allocation> findByRequest_RequestId(Long requestId);

	List<Allocation> findAllByWarehouse_WarehouseId(Long warehouseId);
}