package com.techespals.coachingmanager.Coaching.Application.config;

import com.techespals.coachingmanager.Coaching.Application.entity.Role;
import com.techespals.coachingmanager.Coaching.Application.entity.User;
import com.techespals.coachingmanager.Coaching.Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createSuperAdmin("poovanshi51515@gmail.com", "ASPA@8057", "Poorvanshi");
        createSuperAdmin("yanurag1414@gmail.com", "Lavanu#1", "Anurag");
    }

    private void createSuperAdmin(String email, String password, String name) {
        if (!userRepository.existsByEmail(email)) {
            User superAdmin = User.builder()
                    .name(name)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .role(Role.SUPER_ADMIN)
                    .institute(null)
                    .build();

            userRepository.save(superAdmin);
            System.out.println("SUPER ADMIN CREATED: " + email);
        }
    }
}