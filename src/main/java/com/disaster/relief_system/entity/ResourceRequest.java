package com.disaster.relief_system.entity;

import com.disaster.relief_system.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "resource_requests")
@Data
public class ResourceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    private ReliefCamp reliefCamp;

    private LocalDate requestDate;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private int priorityLevel;

    private String resourceType;

    private int quantity;
}