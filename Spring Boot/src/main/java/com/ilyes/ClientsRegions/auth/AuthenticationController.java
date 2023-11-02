package com.ilyes.ClientsRegions.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilyes.ClientsRegions.Errors.CustomAuthenticationException;
import com.ilyes.ClientsRegions.Services.AuthenticationService;
import com.ilyes.ClientsRegions.config.JwtService;
import com.ilyes.ClientsRegions.controllers.ImageUploadController;
import com.ilyes.ClientsRegions.entities.Role;
import com.ilyes.ClientsRegions.entities.User;
import com.ilyes.ClientsRegions.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserDetailsService userDetailsService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    if(service.register(request) == null) {
      String emailException = "User already exists with email: ";
      return ResponseEntity.badRequest().body(Collections.singletonMap("message", emailException + request.getEmail()));
    }
    return ResponseEntity.ok(Collections.singletonMap("message", "User created successfully"));
  }


  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) throws CustomAuthenticationException {
    System.out.println("authenticate:  " + request.getEmail() + " " + request.getPassword());
    var authResponse = service.authenticate(request, response);
    if (authResponse == null || authResponse.getToken() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(Map.of("error", "Invalid email or password"));

    }
    return ResponseEntity.ok(Collections.singletonMap("token", authResponse.getToken()));
  }
  @PostMapping("/validateToken")
  public ResponseEntity<?> validerToken(HttpServletRequest request) {
    try {
      String token = request.getHeader("Authorization").substring(7);

      final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.extractUsername(token));
      boolean estValide = jwtService.isTokenValid(token, userDetails);

      if (estValide) {
        return ResponseEntity.ok(Collections.singletonMap("state", true));
      } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } catch (Exception err) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
