package com.disaster.relief_system.controller;

import com.disaster.relief_system.entity.ReliefCamp;
import com.disaster.relief_system.service.CampService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camps")
public class CampController {

    private final CampService service;

    public CampController(CampService service) {
        this.service = service;
    }

    // Add Camp
    @PostMapping
    public ReliefCamp addCamp(@RequestBody ReliefCamp camp) {
        return service.addCamp(camp);
    }

    // Get All Camps
    @GetMapping
    public List<ReliefCamp> getAllCamps() {
        return service.getAllCamps();
    }

    // Get Camp By ID
    @GetMapping("/{id}")
    public ReliefCamp getCamp(@PathVariable Long id) {
        return service.getCampById(id);
    }

    // Get Camp By Manager ID
    @GetMapping("/manager/{managerId}")
    public ReliefCamp getCampByManager(@PathVariable Long managerId) {
        return service.getCampByManagerId(managerId);
    }

    // Update Camp
    @PutMapping("/{id}")
    public ReliefCamp updateCamp(@PathVariable Long id,
                                 @RequestBody ReliefCamp camp) {
        return service.updateCamp(id, camp);
    }

    // Update Population
    @PutMapping("/population/{id}/{value}")
    public String updatePopulation(@PathVariable Long id,
                                   @PathVariable int value) {
        service.updatePopulation(id, value);
        return "Population Updated";
    }

    // Update Severity
    @PutMapping("/severity/{id}/{value}")
    public String updateSeverity(@PathVariable Long id,
                                 @PathVariable int value) {
        service.updateSeverity(id, value);
        return "Severity Updated";
    }

    // Delete Camp
    @DeleteMapping("/{id}")
    public String deleteCamp(@PathVariable Long id) {
        service.deleteCamp(id);
        return "Camp Deleted";
    }
}