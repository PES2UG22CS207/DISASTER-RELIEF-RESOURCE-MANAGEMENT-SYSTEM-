package com.disaster.relief_system.entity;

import com.disaster.relief_system.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "deliveries")
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @OneToOne
    private Allocation allocation;

    private String vehicleNumber;

    private String driverName;

    private String destinationDistrict;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private LocalDate dispatchDate;

    private LocalDate deliveredDate;
}