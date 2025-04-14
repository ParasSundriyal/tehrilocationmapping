package com.auth.repository;

import com.auth.model.EmergencyContact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyContactRepository extends MongoRepository<EmergencyContact, String> {
    List<EmergencyContact> findByDistrictAndServiceTypeAndIsActiveTrue(String district, String serviceType);
    List<EmergencyContact> findByDistrictAndIsActiveTrue(String district);
    List<EmergencyContact> findByIsActiveTrue();
} 