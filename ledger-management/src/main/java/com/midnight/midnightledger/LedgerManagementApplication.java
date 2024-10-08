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
    public static void main(String[] args) {
        SpringApplication.run(LedgerManagementApplication.class, args);
    }


    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create Admin User
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User adminUser = User.builder()
                        .password(passwordEncoder.encode("adminPassword")) // Use a secure password
                        .email("admin@example.com")
                        .firstName("admin")
                        .lastName("default")
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(adminUser);
                System.out.println("Admin user created: " + adminUser.getUsername());
            } else {
                System.out.println("Admin user already exists: ");
            }

            // Create Regular User
            if (userRepository.findByEmail("user@example.com").isEmpty()) {
                User regularUser = User.builder()
                        .password(passwordEncoder.encode("userPassword")) // Use a secure password
                        .email("user@example.com")
                        .firstName("user")
                        .lastName("default")
                        .role(Role.USER)
                        .build();
                userRepository.save(regularUser);
                System.out.println("Regular user created: " + regularUser.getUsername());
            } else {
                System.out.println("Regular user already exists: ");
            }
        };
    }
}
