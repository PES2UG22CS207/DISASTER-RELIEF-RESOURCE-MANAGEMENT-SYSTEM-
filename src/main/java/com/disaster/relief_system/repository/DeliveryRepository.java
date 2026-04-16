package com.disaster.relief_system.repository;

import com.disaster.relief_system.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

	Optional<Delivery> findByAllocation_Request_RequestId(Long requestId);

	void deleteByAllocation_AllocationId(Long allocationId);
}