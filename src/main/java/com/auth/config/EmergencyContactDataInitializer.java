package com.auth.config;

import com.auth.model.EmergencyContact;
import com.auth.repository.EmergencyContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class EmergencyContactDataInitializer implements CommandLineRunner {

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    @Override
    public void run(String... args) {
        // Clear existing data
        emergencyContactRepository.deleteAll();

        // Create emergency contacts
        List<EmergencyContact> contacts = Arrays.asList(
            // Police Contacts
            createContact("Bagi Police Station", "8273577685", "Police", "Tehri Garwhal", 
                "Bagi Police Station - Local Police Department", "paras.starkmarkup@gmail.com", 
                "Bagirathipuram, Tehri Garwhal", "24/7 emergency response")
        );

        // Save all contacts
        emergencyContactRepository.saveAll(contacts);
        
        System.out.println("Emergency contacts have been initialized with " + contacts.size() + " records");
    }

    private EmergencyContact createContact(String name, String phoneNumber, String serviceType, 
                                         String district, String description, String email, 
                                         String address, String notes) {
        EmergencyContact contact = new EmergencyContact();
        contact.setName(name);
        contact.setPhoneNumber(phoneNumber);
        contact.setServiceType(serviceType);
        contact.setDistrict(district);
        contact.setDescription(description);
        contact.setEmail(email);
        contact.setAddress(address);
        contact.setNotes(notes);
        contact.setActive(true);
        return contact;
    }
} 