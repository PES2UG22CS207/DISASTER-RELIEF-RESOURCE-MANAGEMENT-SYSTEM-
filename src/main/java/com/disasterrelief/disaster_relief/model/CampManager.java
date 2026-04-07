package com.disasterrelief.disaster_relief.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "camp_managers")
@Getter @Setter @NoArgsConstructor
public class CampManager extends User {

    private String campName;

}