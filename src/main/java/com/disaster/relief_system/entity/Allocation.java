package com.disaster.relief_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "allocations")
@Data
public class Allocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allocationId;

    @OneToOne
    private ResourceRequest request;

    @ManyToOne
    private Warehouse warehouse;

    private LocalDate allocationDate;
}