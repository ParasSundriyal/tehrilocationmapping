package com.auth.repository;

import com.auth.model.EmergencyContact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyContactRepository extends MongoRepository<EmergencyContact, String> {
    
    List<EmergencyContact> findByDistrictAndIsActive(String district, boolean isActive);
    
    List<EmergencyContact> findByServiceTypeAndIsActive(String serviceType, boolean isActive);
    
    List<EmergencyContact> findByDistrictAndServiceTypeAndIsActive(String district, String serviceType, boolean isActive);
    
    List<EmergencyContact> findByIsActive(boolean isActive);
} 