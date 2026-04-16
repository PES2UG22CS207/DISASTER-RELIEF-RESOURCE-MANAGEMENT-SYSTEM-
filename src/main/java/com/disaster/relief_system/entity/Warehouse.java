package com.disaster.relief_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "warehouses")
@Data
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long warehouseId;

    private String warehouseName;

    private String district;

    private String state;

    private int capacity;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}