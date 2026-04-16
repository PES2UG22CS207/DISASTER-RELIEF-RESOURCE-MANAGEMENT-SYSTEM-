package com.disaster.relief_system.service;

import com.disaster.relief_system.entity.Delivery;

import java.util.List;

public interface DeliveryService {

    Delivery createDelivery(Delivery delivery);

    List<Delivery> getAllDeliveries();

    Delivery getDeliveryById(Long id);

    void markPreparing(Long id);

    void markInTransit(Long id);

    void markDelivered(Long id);

    List<Delivery> getPendingDeliveries();

    List<Delivery> getDeliveredDeliveries();
}