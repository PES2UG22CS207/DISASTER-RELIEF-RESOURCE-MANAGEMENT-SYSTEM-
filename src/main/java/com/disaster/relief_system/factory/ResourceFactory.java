package com.disaster.relief_system.factory;

import com.disaster.relief_system.entity.Resource;
import com.disaster.relief_system.enums.ResourceCategory;

public class ResourceFactory {

    public static Resource createResource(
            String name,
            ResourceCategory category,
            String unit,
            String expiryDate) {

        Resource resource = new Resource();

        resource.setResourceName(name);
        resource.setCategory(category);
        resource.setUnit(unit);
        resource.setExpiryDate(expiryDate);

        return resource;
    }
}