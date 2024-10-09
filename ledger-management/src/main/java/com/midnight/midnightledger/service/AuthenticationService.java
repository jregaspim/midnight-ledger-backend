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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AppSettingsRepository appSettingsRepository;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        User user = createUserFromRequest(request);
        userRepository.save(user);

        AppSettings settings = createDefaultAppSettings(user.getId());
        appSettingsRepository.save(settings);

        String jwtToken = jwtService.generateToken(user);
        log.info("User registered with email: {}", user.getEmail());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));

        String jwtToken = jwtService.generateToken(user);
        log.info("User authenticated with email: {}", user.getEmail());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private User createUserFromRequest(RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
    }

    private AppSettings createDefaultAppSettings(Long accountId) {
        return new AppSettings(accountId, "₱", false);
    }
}
