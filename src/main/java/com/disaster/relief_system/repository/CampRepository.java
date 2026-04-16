package com.disaster.relief_system.repository;

import com.disaster.relief_system.entity.ReliefCamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampRepository extends JpaRepository<ReliefCamp, Long> {

    @Query("SELECT c.reliefCamp FROM CampManager c WHERE c.userId = :managerId")
    ReliefCamp findByCampManager_UserId(@Param("managerId") Long managerId);
}