package com.ilyes.ClientsRegions.Services;

// Importing required classes
import com.ilyes.ClientsRegions.entities.EmailDetails;

// Interface
public interface EmailService {
    // To send a simple email
    String sendSimpleMail(EmailDetails details);
}

