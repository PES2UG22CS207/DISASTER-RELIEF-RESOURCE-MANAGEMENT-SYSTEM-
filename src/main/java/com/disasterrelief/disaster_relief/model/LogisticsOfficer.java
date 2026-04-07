package com.disasterrelief.disaster_relief.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "logistics_officers")
@Getter
@Setter
@NoArgsConstructor
public class LogisticsOfficer extends User {

    private String assignedRegion;

}