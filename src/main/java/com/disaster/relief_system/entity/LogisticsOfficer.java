package com.disaster.relief_system.entity;

import com.disaster.relief_system.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "logistics_officers")
@Data
public class LogisticsOfficer extends User {

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    public LogisticsOfficer() {
        setRole(Role.LOGISTICS_OFFICER);
    }
}