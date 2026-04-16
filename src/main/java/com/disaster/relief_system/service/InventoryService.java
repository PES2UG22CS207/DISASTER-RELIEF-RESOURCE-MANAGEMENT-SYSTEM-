package com.disaster.relief_system.service;

import com.disaster.relief_system.entity.InventoryItem;

import java.util.List;

public interface InventoryService {

    List<InventoryItem> getAllInventory();

    List<InventoryItem> getInventoryByWarehouse(Long warehouseId);

    List<InventoryItem> getLowStockItems();

    InventoryItem addInventory(InventoryItem item);

    void increaseStock(Long id, int qty);

    void decreaseStock(Long id, int qty);

    void updateQuantity(Long id, int qty);
}