package com.disaster.relief_system.service;

import com.disaster.relief_system.entity.ReliefCamp;

import java.util.List;

public interface CampService {

    ReliefCamp addCamp(ReliefCamp camp);

    List<ReliefCamp> getAllCamps();

    ReliefCamp getCampById(Long id);

    ReliefCamp getCampByManagerId(Long managerId);

    ReliefCamp updateCamp(Long id, ReliefCamp camp);

    void updatePopulation(Long id, int population);

    void updateSeverity(Long id, int severity);

    void deleteCamp(Long id);
}