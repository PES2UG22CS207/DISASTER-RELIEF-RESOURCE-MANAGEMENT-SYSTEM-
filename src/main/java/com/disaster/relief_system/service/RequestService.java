package com.disaster.relief_system.service;

import com.disaster.relief_system.entity.ResourceRequest;

import java.util.List;

public interface RequestService {

    List<ResourceRequest> getAllRequests();

    List<ResourceRequest> getRequestsByManager(Long managerId);

    ResourceRequest createRequest(ResourceRequest request);

    void approveRequest(Long id);

    void approveRequest(Long id, boolean allowAlternativeWarehouse);

    void rejectRequest(Long id);

    void autoAllocateRequest(Long id);

    void markDelivered(Long id);

    ResourceRequest getRequestById(Long id);

    List<ResourceRequest> getRequestsByCampId(Long campId);
}