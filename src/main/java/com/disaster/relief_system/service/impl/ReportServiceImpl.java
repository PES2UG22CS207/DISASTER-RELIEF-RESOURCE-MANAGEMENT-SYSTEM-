package com.disaster.relief_system.service.impl;

import com.disaster.relief_system.repository.CampRepository;
import com.disaster.relief_system.repository.RequestRepository;
import com.disaster.relief_system.repository.WarehouseRepository;
import com.disaster.relief_system.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private final CampRepository campRepository;
    private final RequestRepository requestRepository;
    private final WarehouseRepository warehouseRepository;

    public ReportServiceImpl(CampRepository campRepository,
                             RequestRepository requestRepository,
                             WarehouseRepository warehouseRepository) {
        this.campRepository = campRepository;
        this.requestRepository = requestRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Object totalCampsReport() {
        return campRepository.count();
    }

    @Override
    public Object pendingRequestsReport() {
        return "Pending Requests Report";
    }

    @Override
    public Object deliveredRequestsReport() {
        return "Delivered Requests Report";
    }

    @Override
    public Object warehouseStockSummary() {

        Map<String, Object> map = new HashMap<>();
        map.put("totalWarehouses", warehouseRepository.count());

        return map;
    }

    @Override
    public Object monthlyReliefReport() {
        return "Monthly Relief Report";
    }
}