package com.disaster.relief_system.repository;

import com.disaster.relief_system.entity.WarehouseManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseManagerRepository extends JpaRepository<WarehouseManager, Long> {

    WarehouseManager findByEmail(String email);

    List<WarehouseManager> findAllByWarehouse_WarehouseId(Long warehouseId);
}
