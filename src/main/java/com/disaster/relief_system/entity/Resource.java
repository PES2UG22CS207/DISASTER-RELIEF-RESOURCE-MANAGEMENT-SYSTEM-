package com.disaster.relief_system.entity;

import com.disaster.relief_system.enums.ResourceCategory;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "resources")
@Data
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    private String resourceName;

    @Enumerated(EnumType.STRING)
    private ResourceCategory category;

    private String unit;

    private String expiryDate;
}