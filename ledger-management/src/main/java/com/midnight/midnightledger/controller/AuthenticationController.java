package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.dto.request.AuthenticationRequest;
import com.midnight.midnightledger.model.dto.request.RegisterRequest;
import com.midnight.midnightledger.model.dto.response.AuthenticationResponse;
import com.midnight.midnightledger.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
