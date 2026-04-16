package com.disaster.relief_system.strategy;

import com.disaster.relief_system.entity.ReliefCamp;
import com.disaster.relief_system.entity.Warehouse;

import java.util.List;

public class NearestWarehouseStrategy
        implements AllocationStrategy {

    @Override
    public Warehouse choose(
            ReliefCamp camp,
            List<Warehouse> warehouses) {

        for (Warehouse w : warehouses) {
            if (w.getDistrict()
                    .equalsIgnoreCase(
                            camp.getDistrict())) {
                return w;
            }
        }

        return warehouses.get(0);
    }
}