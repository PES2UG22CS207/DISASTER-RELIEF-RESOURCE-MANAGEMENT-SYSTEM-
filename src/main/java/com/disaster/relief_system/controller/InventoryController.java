package com.disaster.relief_system.controller;

import com.disaster.relief_system.entity.InventoryItem;
import com.disaster.relief_system.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    // 1. Get All Inventory
    @GetMapping
    public List<InventoryItem> getAllInventory() {
        return service.getAllInventory();
    }

    // 2. Get Inventory By Warehouse
    @GetMapping("/warehouse/{warehouseId}")
    public List<InventoryItem> getByWarehouse(@PathVariable Long warehouseId) {
        return service.getInventoryByWarehouse(warehouseId);
    }

    // 3. Low Stock Alerts
    @GetMapping("/low-stock")
    public List<InventoryItem> lowStock() {
        return service.getLowStockItems();
    }

    // 4. Add Inventory Item
    @PostMapping
    public InventoryItem addInventory(@RequestBody InventoryItem item) {
        return service.addInventory(item);
    }

    // 5. Increase Stock
    @PostMapping("/increase/{id}/{qty}")
    public String increase(@PathVariable Long id,
                           @PathVariable int qty) {
        service.increaseStock(id, qty);
        return "Stock Increased";
    }

    // 6. Decrease Stock
    @PostMapping("/decrease/{id}/{qty}")
    public String decrease(@PathVariable Long id,
                           @PathVariable int qty) {
        service.decreaseStock(id, qty);
        return "Stock Decreased";
    }

    // 7. Update Stock Quantity
    @PutMapping("/{id}/{qty}")
    public String updateQuantity(@PathVariable Long id,
                                 @PathVariable int qty) {
        service.updateQuantity(id, qty);
        return "Stock Updated";
    }
}