package com.auth.service;

import com.auth.model.EmergencyContact;
import com.auth.repository.EmergencyContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmergencyContactService {

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    public EmergencyContact createEmergencyContact(EmergencyContact contact) {
        return emergencyContactRepository.save(contact);
    }

    public List<EmergencyContact> getAllEmergencyContacts() {
        return emergencyContactRepository.findAll();
    }

    public Optional<EmergencyContact> getEmergencyContactById(String id) {
        return emergencyContactRepository.findById(id);
    }

    public EmergencyContact updateEmergencyContact(String id, EmergencyContact updatedContact) {
        if (emergencyContactRepository.existsById(id)) {
            updatedContact.setId(id);
            return emergencyContactRepository.save(updatedContact);
        }
        return null;
    }

    public void deleteEmergencyContact(String id) {
        emergencyContactRepository.deleteById(id);
    }

    public List<EmergencyContact> getActiveContactsByDistrict(String district) {
        return emergencyContactRepository.findByDistrictAndIsActive(district, true);
    }

    public List<EmergencyContact> getActiveContactsByServiceType(String serviceType) {
        return emergencyContactRepository.findByServiceTypeAndIsActive(serviceType, true);
    }

    public List<EmergencyContact> getActiveContactsByDistrictAndServiceType(String district, String serviceType) {
        return emergencyContactRepository.findByDistrictAndServiceTypeAndIsActive(district, serviceType, true);
    }

    public void deactivateContact(String id) {
        emergencyContactRepository.findById(id).ifPresent(contact -> {
            contact.setActive(false);
            emergencyContactRepository.save(contact);
        });
    }

    public void activateContact(String id) {
        emergencyContactRepository.findById(id).ifPresent(contact -> {
            contact.setActive(true);
            emergencyContactRepository.save(contact);
        });
    }
} 