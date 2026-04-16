package com.disaster.relief_system.repository;

import com.disaster.relief_system.entity.RequestItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestItemRepository extends JpaRepository<RequestItem, Long> {

	void deleteByRequest_RequestId(Long requestId);
}