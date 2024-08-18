package org.ahmet.sbpostgdocrest.controller;

import org.ahmet.sbpostgdocrest.model.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Simplified logic: Always return success message
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        return response;
    }
}