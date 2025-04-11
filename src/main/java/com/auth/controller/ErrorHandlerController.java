package com.auth.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ErrorHandlerController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Map<String, Object> errorDetails = new HashMap<>();
        
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        errorDetails.put("error", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            errorDetails.put("status", statusCode);
            errorDetails.put("message", HttpStatus.valueOf(statusCode).getReasonPhrase());
            
            return ResponseEntity.status(statusCode).body(errorDetails);
        }
        
        errorDetails.put("status", 500);
        errorDetails.put("message", "Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }
} 