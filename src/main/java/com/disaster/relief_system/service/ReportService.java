package com.disaster.relief_system.service;

public interface ReportService {

    Object totalCampsReport();

    Object pendingRequestsReport();

    Object deliveredRequestsReport();

    Object warehouseStockSummary();

    Object monthlyReliefReport();
}