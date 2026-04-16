package com.disaster.relief_system.strategy;

import com.disaster.relief_system.entity.ReliefCamp;
import com.disaster.relief_system.entity.Warehouse;

import java.util.List;

public interface AllocationStrategy {

    Warehouse choose(ReliefCamp camp,
                     List<Warehouse> warehouses);
}