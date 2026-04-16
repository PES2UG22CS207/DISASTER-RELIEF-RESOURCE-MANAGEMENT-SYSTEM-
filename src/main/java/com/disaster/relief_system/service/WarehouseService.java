package com.disaster.relief_system.service;

import com.disaster.relief_system.entity.Warehouse;

import java.util.List;

public interface WarehouseService {

    List<Warehouse> getAllWarehouses();

    Warehouse getWarehouseById(Long id);

    Warehouse addWarehouse(Warehouse warehouse);

    Warehouse updateWarehouse(Long id, Warehouse warehouse);

    void deleteWarehouse(Long id);
}