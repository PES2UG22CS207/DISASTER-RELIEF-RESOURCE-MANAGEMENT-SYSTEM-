package com.disaster.relief_system.service.impl;

import com.disaster.relief_system.entity.InventoryItem;
import com.disaster.relief_system.repository.InventoryRepository;
import com.disaster.relief_system.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;

    public InventoryServiceImpl(InventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<InventoryItem> getAllInventory() {
        return repository.findAll();
    }

    @Override
    public List<InventoryItem> getInventoryByWarehouse(Long warehouseId) {
        return repository.findAll()
                .stream()
                .filter(i -> i.getWarehouse().getWarehouseId().equals(warehouseId))
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryItem> getLowStockItems() {
        return repository.findAll()
                .stream()
                .filter(i -> i.getQuantity() < 10)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryItem addInventory(InventoryItem item) {
        return repository.save(item);
    }

    @Override
    public void increaseStock(Long id, int qty) {
        InventoryItem item = repository.findById(id).orElseThrow();
        item.setQuantity(item.getQuantity() + qty);
        repository.save(item);
    }

    @Override
    public void decreaseStock(Long id, int qty) {
        InventoryItem item = repository.findById(id).orElseThrow();
        item.setQuantity(item.getQuantity() - qty);
        repository.save(item);
    }

    @Override
    public void updateQuantity(Long id, int qty) {
        InventoryItem item = repository.findById(id).orElseThrow();
        item.setQuantity(qty);
        repository.save(item);
    }
}