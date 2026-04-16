package com.disaster.relief_system.controller;

import com.disaster.relief_system.entity.Warehouse;
import com.disaster.relief_system.entity.WarehouseManager;
import com.disaster.relief_system.entity.User;
import com.disaster.relief_system.repository.UserRepository;
import com.disaster.relief_system.repository.WarehouseManagerRepository;
import com.disaster.relief_system.service.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService service;
    private final WarehouseManagerRepository warehouseManagerRepository;
    private final UserRepository userRepository;

    public WarehouseController(WarehouseService service,
                               WarehouseManagerRepository warehouseManagerRepository,
                               UserRepository userRepository) {
        this.service = service;
        this.warehouseManagerRepository = warehouseManagerRepository;
        this.userRepository = userRepository;
    }

    // 1. Get All Warehouses
    @GetMapping
    public List<Warehouse> getAllWarehouses() {
        return service.getAllWarehouses();
    }

    // 2. Get Warehouse By ID
    @GetMapping("/{id}")
    public Warehouse getWarehouseById(@PathVariable Long id) {
        return service.getWarehouseById(id);
    }

    // 3. Add New Warehouse
    @PostMapping
    public Warehouse addWarehouse(@RequestBody Warehouse warehouse) {
        return service.addWarehouse(warehouse);
    }

    // 3b. Warehouse Manager: Create & link to my account
    @PostMapping("/my")
    public ResponseEntity<?> createMyWarehouse(@RequestBody Warehouse warehouse, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        WarehouseManager manager = warehouseManagerRepository.findByEmail(authentication.getName());
        if (manager == null) {
            User user = userRepository.findByEmail(authentication.getName());
            if (user != null) {
                manager = warehouseManagerRepository.findById(user.getUserId()).orElse(null);
            }
        }

        if (manager == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Warehouse manager account not found");
        }
        if (manager.getWarehouse() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("This manager already has a linked warehouse");
        }

        if (warehouse.getWarehouseName() == null || warehouse.getWarehouseName().isBlank()
                || warehouse.getDistrict() == null || warehouse.getDistrict().isBlank()
                || warehouse.getState() == null || warehouse.getState().isBlank()
                || warehouse.getCapacity() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Warehouse name, district, state and valid capacity are required");
        }

        Warehouse created = service.addWarehouse(warehouse);
        manager.setWarehouse(created);
        warehouseManagerRepository.save(manager);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 3c. Warehouse Manager: View my warehouse
    @GetMapping("/my")
    public Warehouse getMyWarehouse(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        WarehouseManager manager = warehouseManagerRepository.findByEmail(authentication.getName());
        if (manager != null) {
            return manager.getWarehouse();
        }
        return null;
    }

    // 4. Update Warehouse
    @PutMapping("/{id}")
    public Warehouse updateWarehouse(@PathVariable Long id,
                                     @RequestBody Warehouse warehouse) {
        return service.updateWarehouse(id, warehouse);
    }

    // 5. Delete Warehouse
    @DeleteMapping("/{id}")
    public String deleteWarehouse(@PathVariable Long id) {
        service.deleteWarehouse(id);
        return "Warehouse Deleted Successfully";
    }

    // 6. Get Warehouse Inventory
    @GetMapping("/{id}/inventory")
    public String getWarehouseInventory(@PathVariable Long id) {
        return "Inventory of Warehouse ID: " + id;
    }
}