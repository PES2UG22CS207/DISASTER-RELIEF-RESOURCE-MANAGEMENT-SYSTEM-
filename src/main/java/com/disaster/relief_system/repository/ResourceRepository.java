package com.disaster.relief_system.repository;

import com.disaster.relief_system.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}