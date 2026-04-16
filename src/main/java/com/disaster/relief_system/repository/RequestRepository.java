package com.disaster.relief_system.repository;

import com.disaster.relief_system.entity.ResourceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<ResourceRequest, Long> {

	List<ResourceRequest> findByReliefCamp_CampId(Long campId);
}