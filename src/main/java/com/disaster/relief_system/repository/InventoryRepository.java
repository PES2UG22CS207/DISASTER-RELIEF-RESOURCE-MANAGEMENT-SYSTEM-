package com.disaster.relief_system.repository;

import com.disaster.relief_system.entity.InventoryItem;
import com.disaster.relief_system.enums.ResourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

	List<InventoryItem> findByWarehouse_WarehouseId(Long warehouseId);

	InventoryItem findFirstByWarehouse_WarehouseIdAndResource_Category(Long warehouseId, ResourceCategory category);

	List<InventoryItem> findByWarehouse_WarehouseIdAndResource_Category(Long warehouseId, ResourceCategory category);

	void deleteByWarehouse_WarehouseId(Long warehouseId);
}