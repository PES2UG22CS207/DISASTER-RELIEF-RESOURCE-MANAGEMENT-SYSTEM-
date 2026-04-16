package com.disaster.relief_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "allocation_items")
@Data
public class AllocationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allocationItemId;

    @ManyToOne
    private Allocation allocation;

    @ManyToOne
    private Resource resource;

    private int quantityAllocated;
}