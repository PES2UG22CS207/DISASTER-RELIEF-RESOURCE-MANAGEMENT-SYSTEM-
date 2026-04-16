package com.disaster.relief_system.entity;

import com.disaster.relief_system.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "camp_managers")
@Data
public class CampManager extends User {

    @OneToOne
    @JoinColumn(name = "camp_id")
    private ReliefCamp reliefCamp;

    public CampManager() {
        setRole(Role.CAMP_MANAGER);
    }
}