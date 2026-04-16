package com.disaster.relief_system.controller;

import com.disaster.relief_system.entity.Resource;
import com.disaster.relief_system.service.ResourceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    // 1. Get All Resources
    @GetMapping
    public List<Resource> getAllResources() {
        return service.getAllResources();
    }

    // 2. Get Resource By ID
    @GetMapping("/{id}")
    public Resource getResourceById(@PathVariable Long id) {
        return service.getResourceById(id);
    }

    // 3. Add New Resource
    @PostMapping
    public Resource addResource(@RequestBody Resource resource) {
        return service.addResource(resource);
    }

    // 4. Update Resource
    @PutMapping("/{id}")
    public Resource updateResource(@PathVariable Long id,
                                   @RequestBody Resource resource) {
        return service.updateResource(id, resource);
    }

    // 5. Delete Resource
    @DeleteMapping("/{id}")
    public String deleteResource(@PathVariable Long id) {
        service.deleteResource(id);
        return "Resource Deleted Successfully";
    }
}