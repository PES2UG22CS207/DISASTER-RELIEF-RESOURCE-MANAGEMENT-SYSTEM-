package com.disaster.relief_system.repository;

import com.disaster.relief_system.entity.User;
import com.disaster.relief_system.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    long countByRole(Role role);
}