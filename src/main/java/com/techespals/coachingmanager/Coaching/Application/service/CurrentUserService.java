package com.techespals.coachingmanager.Coaching.Application.service;
import com.techespals.coachingmanager.Coaching.Application.entity.User;
import com.techespals.coachingmanager.Coaching.Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }

    public Long getCurrentInstituteId() {
        User user = getCurrentUser();

        if (user.getInstitute() == null) {
            throw new RuntimeException("Institute not assigned to this user");
        }

        return user.getInstitute().getId();
    }
}