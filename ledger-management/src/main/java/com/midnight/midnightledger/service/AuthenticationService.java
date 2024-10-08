package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.AppSettings;
import com.midnight.midnightledger.model.User;
import com.midnight.midnightledger.model.dto.request.AuthenticationRequest;
import com.midnight.midnightledger.model.dto.request.RegisterRequest;
import com.midnight.midnightledger.model.dto.response.AuthenticationResponse;
import com.midnight.midnightledger.model.enums.Role;
import com.midnight.midnightledger.repository.AppSettingsRepository;
import com.midnight.midnightledger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AppSettingsRepository appSettingsRepository;


    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();


        userRepository.save(user);

        var settings = AppSettings.builder()
                .accountId(user.getId())
                .currency("₱")
                .notificationPreferences(false)
                .build();

        appSettingsRepository.save(settings);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
