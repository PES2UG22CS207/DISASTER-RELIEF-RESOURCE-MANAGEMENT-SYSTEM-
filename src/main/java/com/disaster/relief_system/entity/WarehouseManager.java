package com.disaster.relief_system.entity;

import com.disaster.relief_system.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "warehouse_managers")
@Data
public class WarehouseManager extends User {

    @OneToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    public WarehouseManager() {
        setRole(Role.WAREHOUSE_MANAGER);
    }
}