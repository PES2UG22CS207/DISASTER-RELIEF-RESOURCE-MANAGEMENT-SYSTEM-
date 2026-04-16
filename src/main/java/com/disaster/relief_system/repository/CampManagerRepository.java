package com.disaster.relief_system.repository;

import com.disaster.relief_system.entity.CampManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampManagerRepository extends JpaRepository<CampManager, Long> {

    List<CampManager> findAllByReliefCamp_CampId(Long campId);
}
