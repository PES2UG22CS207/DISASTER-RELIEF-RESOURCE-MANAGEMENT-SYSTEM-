package com.disaster.relief_system.service.impl;

import com.disaster.relief_system.entity.Resource;
import com.disaster.relief_system.repository.ResourceRepository;
import com.disaster.relief_system.service.ResourceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository repository;

    public ResourceServiceImpl(ResourceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Resource> getAllResources() {
        return repository.findAll();
    }

    @Override
    public Resource getResourceById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Resource addResource(Resource resource) {
        return repository.save(resource);
    }

    @Override
    public Resource updateResource(Long id, Resource resource) {
        resource.setResourceId(id);
        return repository.save(resource);
    }

    @Override
    public void deleteResource(Long id) {
        repository.deleteById(id);
    }
}