package com.disaster.relief_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inventory_items")
@Data
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    @ManyToOne
    private Warehouse warehouse;

    @ManyToOne
    private Resource resource;

    private int quantity;
}