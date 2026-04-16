package com.disaster.relief_system.entity;

import com.disaster.relief_system.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {

    public Admin() {
        setRole(Role.ADMIN);
    }
}