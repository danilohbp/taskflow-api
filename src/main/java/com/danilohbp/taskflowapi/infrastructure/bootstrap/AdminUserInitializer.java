package com.danilohbp.taskflowapi.infrastructure.bootstrap;

import com.danilohbp.taskflowapi.domain.model.User;
import com.danilohbp.taskflowapi.domain.model.UserRole;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserInitializer {

    @Bean
    ApplicationRunner initAdminUser(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        return args -> {

            String adminEmail = "admin@taskflow.com";

            if (userRepository.existsByEmailIgnoreCase(adminEmail)) {
                return;
            }

            User admin = new User(
                    "Admin",
                    adminEmail,
                    passwordEncoder.encode("admin123"),
                    UserRole.ADMIN
            );

            userRepository.save(admin);

            System.out.println("✔ Admin user created: admin@taskflow.com / admin123");
        };
    }
}
