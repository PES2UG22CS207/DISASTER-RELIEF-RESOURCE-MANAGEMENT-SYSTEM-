package com.disaster.relief_system.controller;

import com.disaster.relief_system.entity.ResourceRequest;
import com.disaster.relief_system.service.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService service;

    public RequestController(RequestService service) {
        this.service = service;
    }

    // 1. Get All Requests
    @GetMapping
    public List<ResourceRequest> getAllRequests() {
        return service.getAllRequests();
    }

    // Get Requests By Manager
    @GetMapping("/manager/{managerId}")
    public List<ResourceRequest> getRequestsByManager(@PathVariable Long managerId) {
        return service.getRequestsByManager(managerId);
    }

    // 2. Create New Request
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody ResourceRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.createRequest(request));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // 3. Approve Request
    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveRequest(@PathVariable Long id,
                                            @RequestParam(defaultValue = "false") boolean allowAlternativeWarehouse) {
        try {
            service.approveRequest(id, allowAlternativeWarehouse);
            return ResponseEntity.ok("Request approved and allocated");
        } catch (RuntimeException ex) {
            String message = ex.getMessage() == null ? "Approval failed" : ex.getMessage();
            if (message.startsWith("LOW_STOCK_NEAREST|") || message.startsWith("NO_STOCK_ANY|")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // 4. Reject Request
    @PostMapping("/reject/{id}")
    public String rejectRequest(@PathVariable Long id) {
        service.rejectRequest(id);
        return "Request Rejected Successfully";
    }

    // 5. Auto Allocate Request
    @PostMapping("/auto-allocate/{id}")
    public ResponseEntity<?> autoAllocate(@PathVariable Long id) {
        try {
            service.autoAllocateRequest(id);
            return ResponseEntity.ok("Request Auto Allocated Successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // 6. Mark Request Delivered
    @PostMapping("/mark-delivered/{id}")
    public String markDelivered(@PathVariable Long id) {
        service.markDelivered(id);
        return "Request Marked Delivered";
    }

    // 7. Get Request By ID
    @GetMapping("/{id}")
    public ResourceRequest getRequestById(@PathVariable Long id) {
        return service.getRequestById(id);
    }

    // 8. Get Requests By Camp ID
    @GetMapping("/camp/{campId}")
    public List<ResourceRequest> getRequestsByCampId(@PathVariable Long campId) {
        return service.getRequestsByCampId(campId);
    }
}