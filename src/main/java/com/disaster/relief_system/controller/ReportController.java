package com.disaster.relief_system.controller;

import com.disaster.relief_system.service.ReportService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    // 1. Total Camps Report
    @GetMapping("/total-camps")
    public Object totalCamps() {
        return service.totalCampsReport();
    }

    // 2. Pending Requests Report
    @GetMapping("/pending-requests")
    public Object pendingRequests() {
        return service.pendingRequestsReport();
    }

    // 3. Delivered Requests Report
    @GetMapping("/delivered-requests")
    public Object deliveredRequests() {
        return service.deliveredRequestsReport();
    }

    // 4. Warehouse Stock Summary
    @GetMapping("/warehouse-stock")
    public Object stockSummary() {
        return service.warehouseStockSummary();
    }

    // 5. Monthly Relief Report
    @GetMapping("/monthly")
    public Object monthly() {
        return service.monthlyReliefReport();
    }
}