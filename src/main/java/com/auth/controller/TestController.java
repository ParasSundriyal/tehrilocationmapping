package com.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check MongoDB connection
            boolean mongoConnected = mongoTemplate.getDb().getName() != null;
            response.put("mongodb", mongoConnected ? "connected" : "disconnected");
            response.put("database", mongoTemplate.getDb().getName());
            response.put("status", "UP");
        } catch (Exception e) {
            response.put("mongodb", "error");
            response.put("error", e.getMessage());
            response.put("status", "DOWN");
        }
        
        return response;
    }

    @GetMapping("/mongo-test")
    public Map<String, Object> testMongo() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check basic MongoDB connection
            String dbName = mongoTemplate.getDb().getName();
            response.put("database", dbName);
            
            // Test if we can list collections
            List<String> collections = mongoTemplate.getCollectionNames()
                .stream()
                .collect(Collectors.toList());
            response.put("collections", collections);
            
            // Check if users collection exists and count documents
            if (collections.contains("users")) {
                long userCount = mongoTemplate.getCollection("users").countDocuments();
                response.put("userCount", userCount);
            }
            
            response.put("status", "UP");
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
            response.put("stack", getStackTraceAsString(e));
        }
        
        return response;
    }
    
    private String getStackTraceAsString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
} 