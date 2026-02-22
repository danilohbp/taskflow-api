package com.danilohbp.taskflowapi.application.usecase.user.create;

import com.danilohbp.taskflowapi.domain.exception.BusinessRuleException;
import com.danilohbp.taskflowapi.domain.model.User;
import com.danilohbp.taskflowapi.infrastructure.persistence.jpa.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public CreateUserResult execute(CreateUserCommand cmd) {

        if (cmd == null) throw new IllegalArgumentException("CreateUserCommand is required");

        if (userRepository.existsByEmailIgnoreCase(cmd.email())) {
            throw new BusinessRuleException("Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(cmd.rawPassword());

        User user = new User(
                cmd.name(),
                cmd.email(),
                encodedPassword,
                cmd.role()
        );

        User saved = userRepository.save(user);

        return new CreateUserResult(saved.getId());
    }
}
