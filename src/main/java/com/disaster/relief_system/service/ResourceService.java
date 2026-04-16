package com.disaster.relief_system.service;

import com.disaster.relief_system.entity.Resource;
import java.util.List;

public interface ResourceService {

    List<Resource> getAllResources();

    Resource getResourceById(Long id);

    Resource addResource(Resource resource);

    Resource updateResource(Long id, Resource resource);

    void deleteResource(Long id);
}