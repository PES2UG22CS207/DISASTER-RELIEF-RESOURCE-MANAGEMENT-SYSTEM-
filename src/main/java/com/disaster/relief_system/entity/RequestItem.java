package com.disaster.relief_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "request_items")
@Data
public class RequestItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestItemId;

    @ManyToOne
    private ResourceRequest request;

    @ManyToOne
    private Resource resource;

    private int quantityRequested;
}