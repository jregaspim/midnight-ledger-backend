package com.midnight.midnightledger;

import com.midnight.midnightledger.model.User;
import com.midnight.midnightledger.model.enums.Role;
import com.midnight.midnightledger.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class LedgerManagementApplication {

    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_PASSWORD = "adminPassword";
    private static final String USER_EMAIL = "user@example.com";
    private static final String USER_PASSWORD = "userPassword";

    public static void main(String[] args) {
        SpringApplication.run(LedgerManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            createUser(userRepository, passwordEncoder, ADMIN_EMAIL, ADMIN_PASSWORD, Role.ADMIN);
            createUser(userRepository, passwordEncoder, USER_EMAIL, USER_PASSWORD, Role.USER);
        };
    }

    private void createUser(UserRepository userRepository, PasswordEncoder passwordEncoder, String email, String password, Role role) {
        userRepository.findByEmail(email).ifPresentOrElse(
                user -> System.out.println(role + " user already exists: " + email),
                () -> {
                    User newUser = User.builder()
                            .password(passwordEncoder.encode(password))
                            .email(email)
                            .firstName(role == Role.ADMIN ? "admin" : "user")
                            .lastName("default")
                            .role(role)
                            .build();
                    userRepository.save(newUser);
                    System.out.println(role + " user created: " + newUser.getUsername());
                }
        );
    }
}
