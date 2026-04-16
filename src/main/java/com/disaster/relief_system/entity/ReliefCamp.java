package com.disaster.relief_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "relief_camps")
@Data
public class ReliefCamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campId;

    private String name;

    private String district;

    private String state;

    private int population;

    private int severityLevel;
}