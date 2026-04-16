package com.disaster.relief_system.controller;

import com.disaster.relief_system.entity.Delivery;
import com.disaster.relief_system.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService service;

    public DeliveryController(DeliveryService service) {
        this.service = service;
    }

    // 1. Create Delivery
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Delivery delivery) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.createDelivery(delivery));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // 2. Get All Deliveries
    @GetMapping
    public List<Delivery> getAll() {
        return service.getAllDeliveries();
    }

    // 3. Get Delivery By ID
    @GetMapping("/{id}")
    public Delivery getById(@PathVariable Long id) {
        return service.getDeliveryById(id);
    }

    // 4. Mark Preparing
    @PutMapping("/preparing/{id}")
    public String preparing(@PathVariable Long id) {
        service.markPreparing(id);
        return "Delivery Preparing";
    }

    // 5. Mark In Transit
    @PutMapping("/transit/{id}")
    public String transit(@PathVariable Long id) {
        service.markInTransit(id);
        return "Delivery In Transit";
    }

    // 6. Mark Delivered
    @PutMapping("/{id}/delivered")
    public String delivered(@PathVariable Long id) {
        service.markDelivered(id);
        return "Delivery Delivered";
    }

    // 7. Get Pending Deliveries
    @GetMapping("/pending")
    public List<Delivery> pending() {
        return service.getPendingDeliveries();
    }

    // 8. Get Delivered Deliveries
    @GetMapping("/completed")
    public List<Delivery> completed() {
        return service.getDeliveredDeliveries();
    }
}